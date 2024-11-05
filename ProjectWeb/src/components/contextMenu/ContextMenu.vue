<!-- 右键菜单组件 -->
<template>
  <div ref="container" class="container">
    <slot></slot>
    <!-- 使用teleport使组件的位置可以相对于视口 -->
    <Teleport to="body">
      <Transition @beforeEnter="handleBeforeEnter" @enter="handleEnter" @afterEnter="handleAfterEnter">
        <div
          class="context-menu"
          v-show="visible"
          :style="{ left: x + 'px', top: y + 'px' }"
        >
          <div
            class="menu-item"
            v-for="item in props.menu"
            :key="item.label"
            @click="handleClick(item)"
          >
            <p>{{ item.label }}</p>
          </div>
        </div>
      </Transition>
    </Teleport>
  </div>
</template>

<script setup>
import { onMounted, ref, onUnmounted, onBeforeUnmount } from 'vue'

const props = defineProps({
  menu: {
    type: Array,
    default: [],
  },
})

const emit = defineEmits(['select'])

const container = ref(null)

const x = ref(0)
const y = ref(0)
const visible = ref(false)

const handleClick = (item) => {
  emit('select', item)
}

const handleBeforeEnter=(el)=>{
    el.style.height=0
}

const handleEnter=(el)=>{
    el.style.height='auto'
    const h=el.clientHeight
    el.style.height=0
    requestAnimationFrame(()=>{
        requestAnimationFrame(()=>{
            el.style.height=h+'px'
            el.style.transition='0.2s'
        })
    })
}

const handleAfterEnter=(el)=>{
    el.style.transition='none'
}

const showMenu = (e) => {
  e.preventDefault()
  //阻止打开事件的事件冒泡
  e.stopPropagation()
  x.value = e.clientX
  y.value = e.clientY
  visible.value = true
}

const closeMenu = () => {
  visible.value = false
}

onMounted(() => {
  container.value.addEventListener('contextmenu', showMenu)
  //将这两个事件添加到事件捕获的过程中
  window.addEventListener('click', closeMenu, true)
  window.addEventListener('contextmenu', closeMenu, true)
})

onBeforeUnmount(() => {
  container.value.removeEventListener('contextmenu', closeMenu)
})
</script>

<style scoped lang='scss'>
.context-menu {
  position: fixed;
  background: #ffffff;
  border-radius: 5px;
  box-shadow: 2px 2px 5px rgb(138, 138, 138);
  overflow: hidden;
}

.menu-item {
  padding: 7px 10px;
  font-size: 15px;
  border-radius: 5px;
  cursor: pointer;
  max-width: 150px;
  display: flex;
  flex: 1;
  align-items: center;

  p{
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}

.menu-item:hover {
  background: #2c486d;
  color: #ffffff;
}
</style>