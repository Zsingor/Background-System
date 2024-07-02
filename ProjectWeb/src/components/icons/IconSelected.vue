<template>
  <div class="icon-background">
    <div class="icons-search">
      <el-input placeholder="搜索icon" v-model="keyword"></el-input>
    </div>
    <div class="icons-container">
      <el-scrollbar wrap-class="scrollbar-wrapper">
        <div class="grid">
          <div v-for="(item,index) of currentData" :key="index">
            <div class="icon-item" @click="selectedHandler(item)">
              <div class="icon-com">
                <el-icon >
                  <component :is="item"></component>
                </el-icon>
              </div>
              <div class="icon-span">
                <span>{{ item }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-scrollbar>
    </div>
    <div class="pagination-wrapper">
      <el-pagination background layout="total, sizes, prev, pager, next, jumper"
                     :total="iconCount"
                     :page-sizes="[40, 80, 120, 160, 320]"
                     v-model:pageSize="pagination.pageSize"
                     v-model:currentPage="pagination.currentPage">
      </el-pagination>
    </div>
  </div>
</template>

<script setup>
import {computed, defineProps, reactive, ref} from "vue";

//定义界面的name，用于使用keep-alive
defineOptions({
  name: 'IconSelected'
})

const props = defineProps({
  icons: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(["selectedIcon"]);

// 搜索关键字
const keyword = ref("");

// 根据关键字过滤后最终展示的数据
const showIcons = computed(() => {
  return props.icons.filter((item) => {
    return item.indexOf(keyword.value) !== -1;
  });
});

// 每页展示的数据
const currentData = computed(() => {
  const index = (pagination.currentPage - 1) * pagination.pageSize;
  return showIcons.value.slice(index, index + pagination.pageSize);
});

// 展示的数据数量
const iconCount = computed(() => showIcons.value.length);

// 分页
const pagination = reactive({
  currentPage: 1,
  pageSize: 80
});

// 触发选中事件
const selectedHandler = (item) => {
  emit("selectedIcon", item);
}
</script>

<style scoped>
.icon-background{
  width: 100%;
  height: 100%;
}

.icons-search {
  width: 50%;
  height: 40px;
  margin:0 auto 10px auto;
}

.icons-container {
  height: calc(100% - 100px);
  overflow: hidden;

  .grid {
    padding: 0 20px;
    position: relative;
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  }

  .icon-item {
    margin: 20px;
    height: 85px;
    text-align: center;
    width: 100px;
    float: left;
    color: #24292e;
    cursor: pointer;
    display: flex;
    flex-wrap: wrap;

    &:hover {
      color: #16AAD8FF;
    }
  }

  .icon-com{
    display: flex;
    font-size: 25px;
    justify-content: center;
    align-items: center;
    width: 100%;
  }

  .icon-span {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 14px;
    margin-top: 10px;
  }

  .disabled {
    pointer-events: none;
  }
}

.pagination-wrapper {
  display: flex;
  width: 100%;
  height: 40px;
  margin-top: 10px;
  justify-content: center;
}
</style>
