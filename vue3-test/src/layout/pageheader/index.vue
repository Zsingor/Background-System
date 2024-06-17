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
      <div class="operate-button" @click="refreshpage">
        <el-icon style="font-size: 20px;" title="刷新页面"><RefreshRight /></el-icon>
      </div>
      <div class="operate-button" @click="controls.showGlobalSetting=true">
        <el-icon style="font-size: 20px;" title="设置"><Setting /></el-icon>
      </div>
      <div class="operate-button" >
        <el-icon style="font-size: 20px;" title="清空缓存"><Brush /></el-icon>
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
import {provide, reactive, ref} from "vue";
import { useRouter } from 'vue-router'
import request from "@/request/index.js";
import {logout, userInfo} from "@/layout/user.js";
import {isEmpty} from "@/utils/commons.js";

const router = useRouter()

const username=ref("")

const controls = reactive({
  showUserForm: false,
  showPasswordForm: false,
  showGlobalSetting: false,
});

const refreshpage=()=>{
  userInfo.baseInfo=JSON.parse(localStorage.getItem("User_Info"))
  createRouteAndMenu(userInfo.baseInfo.menuList)
  reloadPage()
}

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