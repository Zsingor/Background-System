<template>
  <div
      :style="{border: '1px solid #ccc', width: width}"
      v-loading="loading"
      element-loading-text="文件上传中..."
  >
    <Toolbar
        style="border-bottom: 1px solid #ccc"
        :editor="editorRef"
        :defaultConfig="toolbarConfig"
    />
    <Editor
        :style="{ height: height, overflowY: 'hidden'}"
        class="EditorDom"
        v-model="editValue"
        :defaultConfig="props.editorConfig"
        @onCreated="handleCreated"
        @onChange="handleChange"
    />
  </div>
</template>

<script setup>
import {Editor, Toolbar} from "@wangeditor/editor-for-vue";
import '@wangeditor/editor/dist/css/style.css'
import {Boot} from "@wangeditor/editor";
import attachmentModule from "@wangeditor/plugin-upload-attachment";
import {computed, onBeforeUnmount, ref, shallowRef} from "vue";
import request from "@/request/index.js";
import {message} from "@/utils/message.js";


const props = defineProps({
  //组件的宽度
  width: {
    type: String,
    default: "calc(100vw)",
  },
  //组件的高度
  height: {
    type: String,
    default: '450px',
  },
  //编辑器的配置
  editorConfig: {
    type: Object,
    default: () => ({
      placeholder: "请输入内容...",
      MENU_CONF: {},
    })
  },
  //编辑器的内容
  modelValue: {
    type: String,
    required: true,
  }
})

const emits = defineEmits(["update:modelValue", "getHtml"]);

// 注册。要在创建编辑器之前注册，且只能注册一次，不可重复注册。
if (Boot.plugins.length < 13) {
  //判断如果已经插入进去，不在二次插入
  Boot.registerModule(attachmentModule);
}

const toolbarConfig = {
  insertKeys: {
    index: 0, // 自定义插入的位置
    keys: ["uploadAttachment"], // “上传附件”菜单
  },
};

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef();

let loading = ref(false)

//为了防止破坏单向数据流，使用computed对modelValue做拦截
const editValue = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emits("update:modelValue", value);
  },
})

const handleCreated = (editor) => {
  editorRef.value = editor; // 记录 editor 实例
}

// 内容改变
const handleChange = (editor) => {
  emits("getHtml", editor.getText(), editor.getHtml());
}

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  if (editorRef.value) {
    editorRef.value.destroy();
  }
})

//销毁dom
const distoryEditor = () => {
  const editor = editorRef.value;
  if (editor == null) return;
  editor.destroy();
}

//实时更新数据
const setWangContent = (val) => {
  editorRef.value.setHtml(val);
}


// 图片上传设置
props.editorConfig.MENU_CONF["uploadImage"] = {
  // 服务端地址
  maxNumberOfFiles: 1,
  allowedFileTypes: ["image/*"],
  async customUpload(file, insertFn) {
    if (!file.type.includes("image")) {
      return message("请上传图片格式的文件！","error")
    }
    if (file.size / 1024 / 1024 > 10) {
      return message("请上传10M以内的文件！","error")
    }
    const formData = new FormData()
    formData.append("file", file)
    loading.value = true
    request.post("/files/uploadEditor", formData)
        .then((res) => {
          const videoInfo = res.data || ""
          const {url, alt} = videoInfo
          if (!url) throw new Error(`Image url is empty`)
          // 自己插入图片
          insertFn(url, alt)
        })
        .finally(() => {
          loading.value = false
        })
  },
}

// 视频上传
props.editorConfig.MENU_CONF["uploadVideo"] = {
  // 最多可上传几个文件，默认为 5
  maxNumberOfFiles: 1,
  async customUpload(file, insertFn) {
    if (!file.type.includes("video")) {
      return message("请上传视频格式的文件！","error")
    } else if (file.size / 1024 / 1024 > 50) {
      return message("请上传50M以内的文件！","error")
    }
    const formData = new FormData()
    formData.append("file", file)
    loading.value = true;
    request.post("/files/uploadEditor", formData)
        .then((res) => {
          const videoInfo  = res.data || ""
          const { url, alt } = videoInfo
          if (!url) throw new Error(`Video url is empty`)
          // 自己插入视频
          insertFn(url, alt)
        })
        .finally(() => {
          loading.value = false
        })
  },
}

//附件上传
props.editorConfig.MENU_CONF["uploadAttachment"] = {
  async customUpload(file, insertFn) {
    if (file.size / 1024 / 1024 > 50) {
      return message("请上传50M以内的文件！","error")
    }
    const formData = new FormData()
    formData.append("file", file);
    loading.value = true
    request.post("/files/uploadEditor", formData)
        .then((res) => {
          const videoInfo  = res.data || ""
          const { url, alt } = videoInfo
          if (!url) throw new Error(`files url is empty`)
          // 自己插入文件
          insertFn(alt,url)
        })
        .finally(() => {
          loading.value = false
        })
  },
}
</script>

<style scoped>

</style>