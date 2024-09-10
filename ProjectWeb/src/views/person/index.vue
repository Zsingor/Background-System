<!-- 个人信息界面 -->
<template>
  <div class="background">
    <div class="top">
      <el-card style="width: 100%; height: 100%">
        <div class="card-top">
          <img style="width: 200px; height: 140px" src="@/assets/work.png"/>
          <div class="userInfo">
            <p class="important-font">{{ user.name }}</p>
            <p class="secondary-font">{{ user.description }}</p>
          </div>
        </div>
      </el-card>
    </div>
    <div class="bottom">
      <el-card style="width: 100%; height: 100%">
        <div class="card-header">
          <span>详细信息</span>
        </div>
        <div class="card-middle">
          <el-form
            ref="formRef"
            :rules="formRules"
            label-width="100px"
            label-position="left"
            :model="user"
          >
            <el-row :gutter="20">
              <el-col :span="16">
                <el-form-item label="用户名:" prop="name">
                  <el-input
                    :disabled="isDisable"
                    size="large"
                    v-model="user.name"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="16">
                <el-form-item label="邮箱:" prop="email">
                  <el-input
                    :disabled="isDisable"
                    size="large"
                    v-model="user.email"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="16">
                <el-form-item label="描述:" prop="description">
                  <el-input
                    :disabled="isDisable"
                    type="textarea"
                    rows="4"
                    size="large"
                    v-model="user.description"
                    clearable
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <div class="card-bottom">
          <el-button type="primary" @click="isDisable = !isDisable">
            修改
          </el-button>
          <el-button type="success" :disabled="isDisable" @click="saveUser">
            保存
          </el-button>
          <el-button type="warning" @click="editpwdIsDialog = true">
            修改密码
          </el-button>
        </div>
      </el-card>
    </div>

    <el-dialog title="修改密码" v-model="editpwdIsDialog" class="editpwdDialog" draggable :show-close="false">
      <el-steps
        :active="pwdactive"
        finish-status="success"
        simple
        style="margin-top: 20px"
      >
        <el-step title="输入当前密码"></el-step>
        <el-step title="输入新密码"></el-step>
      </el-steps>
      <el-form
        :model="editpwd"
        :rules="pwdRules"
        ref="editRef"
        style="margin-top: 20px"
      >
        <el-form-item
          label="当前密码"
          prop="password"
          label-width="100"
          v-if="pwdactive === 0"
        >
          <el-input
            v-model="editpwd.password"
            show-password
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="新密码"
          prop="newpassword"
          label-width="100"
          v-if="pwdactive === 1"
        >
          <el-input
            v-model="editpwd.newpassword"
            show-password
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="确认密码"
          prop="checkpassword"
          label-width="100"
          v-if="pwdactive === 1"
        >
          <el-input
            v-model="editpwd.checkpassword"
            show-password
            autocomplete="off"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="canceledit">取 消</el-button>
          <el-button type="primary" @click="editpwdnext" v-if="pwdactive === 0"
            >下一步</el-button
          >
          <el-button type="primary" @click="editpwdsure" v-if="pwdactive === 1"
            >确 定</el-button
          >
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { getImageUrl } from "@/utils/resource.js";
import request from "@/request/index.js";
import { userInfo } from "@/layout/user.js";
import { isEmpty } from "@/utils/commons.js";
import { message } from "@/utils/message";
import { encodeData } from "@/utils/rsa.js";

defineOptions({
  name: "person",
});

const formRef = ref(null);
const editRef = ref(null);

let loginFlag = ref(false);
//控制是否允许修改
let isDisable = ref(true);
//控制修改密码弹窗显示
let editpwdIsDialog = ref(false);
//控制修改密码的进度
let pwdactive = ref(0);

const checkEmail = (rule, value, callback) => {
  const regEmail = /^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
  if (!regEmail.test(value) && !isEmpty(value)) {
    callback(new Error("请输入正确的邮箱格式"));
  } else {
    callback();
  }
};

const checkPwd = (rule, value, callback) => {
  if (value.length < 6 || value.length > 20) {
    callback(new Error("密码长度为6-20位"));
  } else {
    callback();
  }
};

const checkPassword = (rule, value, callback) => {
  if (value !== editpwd.value["newpassword"]) {
    callback(new Error("两次输入密码不一致"));
  } else {
    callback();
  }
};

const formRules = ref({
  name: [
    { required: true, message: "请输入名称" },
    { min: 1, max: 30, message: "长度在 1 到 30 个字符" },
  ],
  email: [
    { required: false, message: "请输入邮箱" },
    { validator: checkEmail, trigger: "blur" },
  ],
});

const pwdRules = ref({
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { validator: checkPwd, trigger: "blur" },
  ],
  newpassword: [
    { required: true, message: "请输入新密码", trigger: "blur" },
    { validator: checkPwd, trigger: "blur" },
  ],
  checkpassword: [
    { required: true, message: "请确认新密码", trigger: "blur" },
    { validator: checkPassword, trigger: "blur" },
  ],
});

let user = ref({
  id: "",
  name: "游客",
  email: "",
  description: "暂无描述",
});

let editpwd = ref({
  password: "",
  newpassword: "",
  checkpassword: "",
});

const saveUser = () => {
  if (!isDisable.value) {
    formRef.value.validate((valid) => {
      if (valid) {
        request
          .post("/user/updateSelf", user.value)
          .then((res) => {
            if(res.code==1)
            {
                isDisable.value = true;
                message("保存成功");
            }
            else
            {
                message(res.msg, "error");
            }
          })
          .catch((error) => {
            message(error, "error");
          });
      }
    });
  }
}

const canceledit = () => {
  pwdactive.value = 0;
  editpwdIsDialog.value = false;
  editpwd.value = {
    password: "",
    newpassword: "",
    checkpassword: "",
  };
};

//验证密码
const editpwdnext = () => {
  editRef.value.validate((valid) => {
    // 验证通过
    if (valid) {
      let newUser = {};
      newUser.id = user.value.id;
      newUser.name = user.value.name;
      newUser.password = editpwd.value.password;
      newUser.password = encodeData(newUser.password);
      request
        .post("/user/checkpwd", newUser)
        .then((result) => {
          if (result.code === 1) {
            if (pwdactive.value++ > 1) pwdactive.value = 0;
          } else {
            message(result.msg, "error");
          }
        })
        .catch((error) => {
          message(error, "error");
        });
    }
  });
};

const editpwdsure = () => {
  editRef.value.validate((valid) => {
    // 验证通过
    if (valid) {
      let newUser = {};
      newUser.id = user.value.id;
      newUser.name = user.value.name;
      newUser.password = editpwd.value.newpassword;
      newUser.password = encodeData(newUser.password);
      request
        .post("/user/updatePwd", newUser)
        .then((result) => {
          if (result.code === 1) {
            message("密码更新成功");
            pwdactive.value = 0;
            editpwdIsDialog.value = false;
            editpwd.value = {
              password: "",
              newpassword: "",
              checkpassword: "",
            };
          } else {
          }
        })
        .catch((error) => {});
    }
  });
};

const initUser = () => {
  if (loginFlag.value) {
    user.value.id = userInfo.baseInfo.user_id;
    request
      .post("/user/querymsssage", user.value)
      .then((res) => {
        user.value = res.data;
      })
      .catch((error) => {
        console.log(error);
      });
  } else {
    user.value.name = "游客";
    user.value.description = "暂无描述";
  }
};

onMounted(() => {
  userInfo.baseInfo = JSON.parse(localStorage.getItem("User_Info"));
  loginFlag.value = !isEmpty(userInfo.baseInfo);

  initUser();
});
</script>

<style scoped lang='scss'>
.background {
  width: 100%;
  height: 100%;
}

.top {
  width: 100%;
  height: 25%;
}

.bottom {
  width: 100%;
  height: 72%;
  margin-top: 1%;
}

.card-top {
  width: 100%;
  height: 100%;
  display: flex;
  align-content: center;
  align-items: center;
}

.userAvatar {
  margin-left: 30px;
  width: 9%;
  height: 90%;
  border-radius: 50%;
}

.userInfo {
  width: 70%;
  display: flex;
  flex-wrap: wrap;
  align-content: center;
  overflow: hidden;
  padding-left: 2%;
}

.important-font {
  width: 100%;
  font-weight: 900;
  font-size: 25px;
}

.secondary-font {
  color: #909399;
}

.card-header {
  padding: 10px;
  font-size: 18px;
  border-bottom: 2px solid #dcdfe6;
}

.card-middle {
  width: 100%;
  padding-top: 20px;
  padding-left: 2%;
}

.card-bottom {
  width: 100%;
  display: flex;
  bottom: 0;
  justify-content: center;
}
</style>