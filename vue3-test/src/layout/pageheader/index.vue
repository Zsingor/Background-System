<template>
  <div id="app-navbar" class="disable-selected">
    <div class="navbar-left">
      <div class="collapse-button"
           @click="persistentConfig.isCollapse=!persistentConfig.isCollapse">
        <el-icon style="font-size: 20px;">
          <component :is="persistentConfig.isCollapse?'Expand':'Fold'" />
        </el-icon>
      </div>
      <div class="app-logo" >
        {{ projectName }}
      </div>
    </div>
    <div class="navbar-right">
      <div class="header-search">
        <div class="search-select" :class="{'select-hide':activeHide }">
          <el-select
              v-model="selectPath"
              filterable
              remote
              :remote-method="remoteMethod"
              placeholder="搜索菜单"
              @change="choosePath"
          >
            <el-option
                v-for="item in options"
                :key="item.path"
                :label="item.title"
                :value="item.path"
            />
          </el-select>
        </div>
        <div class="operate-button" @click="search">
          <el-icon style="font-size: 20px;" title="搜索"><Search /></el-icon>
        </div>
      </div>
      <div class="operate-button" @click="refreshpage">
        <el-icon style="font-size: 20px;" title="刷新页面"><RefreshRight /></el-icon>
      </div>
      <div class="operate-button" @click="controls.showGlobalSetting=true">
        <el-icon style="font-size: 20px;" title="设置"><Setting /></el-icon>
      </div>
      <div class="operate-button" @click="skipMessage">
        <el-badge v-if="unread_count!==0" class="item-button" :value="unread_count" :max="99">
          <el-icon style="font-size: 20px;" title="我的信息"><Message /></el-icon>
        </el-badge>
        <el-icon v-else style="font-size: 20px;" title="我的信息"><Message /></el-icon>
      </div>
      <div class="user-info">
        <el-dropdown v-if="isLogin()" trigger="click">
          <div class="user-info-text el-dropdown-link">
            我的
            <el-icon><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="logout">
                <el-icon><SwitchButton /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>

        <div v-else @click="logout" class="user-info-text el-dropdown-link">
          登录
        </div>

      </div>
    </div>
    <GlobalSetting/>
  </div>
</template>

<script setup>
import GlobalSetting from "@/layout/pageheader/components/setting/index.vue"
import {projectName} from "@/setup.js";
import {persistentConfig} from "@/layout/layout.js";
import {createRouteAndMenu} from "@/router/routeUtils.js";
import {reloadPage} from "@/layout/tags/tag.js";
import {onMounted, provide, reactive, ref} from "vue";
import { useRouter } from 'vue-router'
import {logout, userInfo} from "@/layout/user.js";
import {isEmpty} from "@/utils/commons.js";
import request from "@/request/index.js";
import {ElNotification} from "element-plus";

const userouter = useRouter()

const username=ref("")
const selectPath=ref("")
let options=ref([])
//控制下拉框显示与否
let activeHide=ref(true)

const controls = reactive({
  showUserForm: false,
  showPasswordForm: false,
  showGlobalSetting: false,
});

//选择路由跳转
const choosePath=()=>{
  activeHide.value=true
  if (!isEmpty(selectPath.value)) {
    userouter.push(`/admin${selectPath.value}`)
  } else {

  }
  selectPath.value=""
}

//搜索下拉框显示
const remoteMethod=(query)=>{
  if(query)
  {
    options.value = userInfo.user_menus.filter((item) => {
      return item.title.toLowerCase().includes(query.toLowerCase())
    })
  }
  else
  {
    options.value = []
  }

}

const search=()=>{
  activeHide.value=!activeHide.value
}


//刷新界面
const refreshpage=()=>{
  userInfo.baseInfo=JSON.parse(localStorage.getItem("User_Info"))
  createRouteAndMenu(userInfo.baseInfo.menuList)
  reloadPage()
}

//跳转至站内信
const skipMessage=()=>{
  userouter.push("/admin/message")
}

//判断是否登录
const isLogin = () => {
  userInfo.baseInfo=JSON.parse(localStorage.getItem("User_Info"))
  if(!isEmpty(userInfo.baseInfo))
  {
    username.value=userInfo.baseInfo.user_name
    return true
  }
  else
  {
    return false
  }
}

provide("controls",controls)

onMounted(()=>{
  request.post("/conversations/getUnreadCount",{id:userInfo.baseInfo.user_id}).then(res => {
    userInfo.unread_count=res.data
    if(userInfo.unread_count>0)
    {
      ElNotification({
        title: "未读消息",
        message: `您有${userInfo.unread_count}条未读消息，请尽快处理`,
        duration:0,
        position: persistentConfig.notiPosition,
        type: 'info',
        onClick(){
          ElNotification.closeAll();
          userouter.push({path:'/admin/message'})
        }
      })
    }
  })

  console.log(userInfo.user_menus)
})

</script>

<style >

/* 菜单栏的样式 */

.el-dropdown-link {
  cursor: pointer;
  color: #f0f0f0;
}

#app-navbar {
  width: 100%;
  height: 48px;
  background: var(--header-bg,#F1F4F3FF);
  color: var(--header-font-color,#000000FF);
  padding-right: 18px;
  position: fixed;
  top: 0;
  left: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .navbar-left {
    height: inherit;
    display: flex;
    align-items: center;

    .collapse-button {
      width: 64px;
      height: inherit;
      display: flex;
      justify-content: center;
      align-items: center;

      &:hover {
        cursor: pointer;
        background: var(--header-bg1,#C7D1CEFF);
      }
    }

    .app-logo {
      margin-left: 8px;
    }

    .app-logo.allow-click {
      cursor: pointer;
    }
  }

  .navbar-right {
    height: inherit;
    display: flex;
    align-items: center;

    .header-search{
      display: flex;
    }

    .search-select{
      width: 200px;
      display: flex;
      justify-content: center;
      align-items: center;
      margin-right: 10px;
    }

    .select-hide{
      width: 0;
      display: none;
    }

    .operate-button {
      width: 38px;
      height: 38px;
      border-radius: 50%;
      display: flex;
      justify-content: center;
      align-items: center;

      &:not(.disable):hover {
        cursor: pointer;
        background: var(--header-bg1,#C7D1CEFF);
      }
    }

    .item-button{
      margin-top: 5px;
    }

    .user-info {
      min-width: 80px;
      height: inherit;
      margin-left: 8px;

      &:hover {
        cursor: pointer;
        background: var(--header-bg1,#C7D1CEFF);
      }

      .user-info-text {
        min-width: 80px;
        padding: 0 8px;
        height: 48px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: var(--header-font-color,#000000FF);
      }
    }
  }
}
</style>