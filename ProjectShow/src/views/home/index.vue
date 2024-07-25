<template>
  <div>
    <!-- 头部 -->
    <header class="header" id="header" :class="{ 'scroll-header': isScrolled }">
      <nav class="nav container">
        <a href="#" class="nav__logo">Singor</a>

        <div class="nav__menu" id="nav-menu">
          <ul class="nav__list grid">
            <li class="nav__item" v-for="section in sections" :key="section.id">
              <a
                :href="`#${section.id}`"
                class="nav__link"
                :class="{ 'active-link': isActive(section.id) }"
              >
                <i :class="`uil ${section.icon}`" class="nav__icon"></i>
                <p>{{ $t(section.label) }}</p>
              </a>
            </li>

            <li class="nav__item">
              <div class="nav__link" @click="changeLang">
                <i class="uil uil-english-to-chinese change-theme"></i>
              </div>
            </li>
          </ul>
          <i class="uil uil-times nav__close" id="nav-close"></i>
        </div>

        <div class="nav__btns">
          <i class="uil uil-moon change-theme" id="theme-button"></i>
          <div class="nav__toggle" id="nav-toggle">
            <i class="uil uil-apps"></i>
          </div>
        </div>
      </nav>
    </header>

    <!-- 主要内容 -->
    <main class="main">
      <section class="section" id="home">
        <div class="home_body">nihao</div>
      </section>

      <section class="section" id="about">
        <div class="home_body1"></div>
      </section>
    </main>

    <!-- 底部 -->
    <footer class="footer">
      <div></div>
    </footer>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref, getCurrentInstance } from "vue";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const { proxy } = getCurrentInstance();

//用于判断头部阴影是否显示
let isScrolled = ref(false);
// 用于检测滚动并激活链接的响应式引用
let activeSection = ref(null);

//用于判断标签
const sections = ref([
  { id: "home", icon: "uil-estate", label: "nav.home" },
  { id: "about", icon: "uil-user", label: "nav.about" },
  { id: "skills", icon: "uil-file-alt", label: "nav.skills" },
  { id: "portfolio", icon: "uil-scenery", label: "nav.portfolio" },
  { id: "contact", icon: "uil-message", label: "nav.contact" },
]);

//切换语言
const changeLang = () => {
  let lang = localStorage.getItem("localeLang")
  let value=null
  if (lang === null) {
    lang='zhCn'
    value=lang
  }
  else{
    value = lang === "zhCn" ? "en" : "zhCn"
  }
  localStorage.setItem("localeLang", value)
  proxy.$i18n.locale = value;
};

// 检查给定ID的section是否处于激活状态
const isActive = (id) => {
  return id === activeSection.value;
};

// 处理滚动事件
const handleScroll = () => {
  isScrolled.value = window.scrollY >= 80;

  const sectionsInView = Array.from(
    document.querySelectorAll(".section")
  ).filter((section) => {
    return (
      section.getBoundingClientRect().top <= window.innerHeight &&
      section.getBoundingClientRect().bottom > 0
    );
  });
  if (sectionsInView.length > 0) {
    activeSection.value = sectionsInView[0].id; // 只激活最上面的可见部分
  } else {
    activeSection.value = null;
  }
};

// 组件挂载后添加滚动监听器
onMounted(() => {
  window.addEventListener("scroll", handleScroll);
});

// 组件卸载前移除滚动监听器，防止内存泄漏
onUnmounted(() => {
  window.removeEventListener("scroll", handleScroll);
});
</script>

<style scoped lang="scss">
@import "./style.scss";
</style>
