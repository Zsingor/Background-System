<template>
  <div>
    <!-- 头部 -->
    <header class="header" id="header" :class="{ 'scroll-header': isScrolled }">
      <nav class="nav container">
        <a href="#" class="nav__logo">Singor</a>

        <div class="nav__menu" id="nav-menu" :class="{ 'show-menu': showmenu }">
          <ul class="nav__list grid">
            <li class="nav__item" v-for="section in sections" :key="section.id">
              <a
                :href="`#${section.id}`"
                class="nav__link"
                @click="showMenu"
                :class="{ 'active-link': isActive(section.id) }"
              >
                <i :class="`uil ${section.icon}`" class="nav__icon"></i>
                <p>{{ $t(section.label) }}</p>
              </a>
            </li>

            <li class="nav__item">
              <div class="nav__link" @click="changeLang">
                <i class="uil uil-english-to-chinese change-lang"></i>
                <p class="nav__title">{{ $t("translate") }}</p>
              </div>
            </li>
          </ul>
          <i
            class="uil uil-times nav__close"
            @click="showMenu"
            id="nav-close"
          ></i>
        </div>

        <div class="nav__btns">
          <i
            @click="changeTheme"
            :class="`uil ${themeIcon}`"
            class="change-theme"
            id="theme-button"
          ></i>
          <div class="nav__toggle" @click="showMenu" id="nav-toggle">
            <i class="uil uil-apps"></i>
          </div>
        </div>
      </nav>
    </header>

    <!--==================== 主要部分 ====================-->
    <main class="main">
      <!--==================== HOME ====================-->
      <section class="home section" id="home">
        <div class="home__container container grid">
          <div class="home__content grid">
            <div class="home__social">
              <a
                href="https://gitee.com/asoutherncat/"
                target="_blank"
                class="home__social-icon"
              >
                <!-- <i class="uil uil-linkedin-alt"></i> -->
                <svg
                  t="1674798484640"
                  viewBox="0 0 1024 1024"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  p-id="2757"
                >
                  <path
                    d="M512 1024C229.222 1024 0 794.778 0 512S229.222 0 512 0s512 229.222 512 512-229.222 512-512 512z m259.149-568.883h-290.74a25.293 25.293 0 0 0-25.292 25.293l-0.026 63.206c0 13.952 11.315 25.293 25.267 25.293h177.024c13.978 0 25.293 11.315 25.293 25.267v12.646a75.853 75.853 0 0 1-75.853 75.853h-240.23a25.293 25.293 0 0 1-25.267-25.293V417.203a75.853 75.853 0 0 1 75.827-75.853h353.946a25.293 25.293 0 0 0 25.267-25.292l0.077-63.207a25.293 25.293 0 0 0-25.268-25.293H417.152a189.62 189.62 0 0 0-189.62 189.645V771.15c0 13.977 11.316 25.293 25.294 25.293h372.94a170.65 170.65 0 0 0 170.65-170.65V480.384a25.293 25.293 0 0 0-25.293-25.267z"
                    fill="hsl(250deg 69% 61%)"
                    p-id="2758"
                  ></path>
                </svg>
              </a>
              <a
                href="http://github.com"
                target="_blank"
                class="home__social-icon"
              >
                <i class="uil uil-github-alt"></i>
              </a>
              <a
                href="https://t.bilibili.com/"
                target="_blank"
                class="home__social-icon"
              >
                <!-- <i class="uil uil-dribbble"></i> -->
                <svg
                  t="1674801734735"
                  viewBox="0 0 1024 1024"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  p-id="4124"
                >
                  <path
                    d="M777.514667 131.669333a53.333333 53.333333 0 0 1 0 75.434667L728.746667 255.829333h49.92A160 160 0 0 1 938.666667 415.872v320a160 160 0 0 1-160 160H245.333333A160 160 0 0 1 85.333333 735.872v-320a160 160 0 0 1 160-160h49.749334L246.4 207.146667a53.333333 53.333333 0 1 1 75.392-75.434667l113.152 113.152c3.370667 3.370667 6.186667 7.04 8.448 10.965333h137.088c2.261333-3.925333 5.12-7.68 8.490667-11.008l113.109333-113.152a53.333333 53.333333 0 0 1 75.434667 0z m1.152 231.253334H245.333333a53.333333 53.333333 0 0 0-53.205333 49.365333l-0.128 4.010667v320c0 28.117333 21.76 51.157333 49.365333 53.162666l3.968 0.170667h533.333334a53.333333 53.333333 0 0 0 53.205333-49.365333l0.128-3.968v-320c0-29.44-23.893333-53.333333-53.333333-53.333334z m-426.666667 106.666666c29.44 0 53.333333 23.893333 53.333333 53.333334v53.333333a53.333333 53.333333 0 1 1-106.666666 0v-53.333333c0-29.44 23.893333-53.333333 53.333333-53.333334z m320 0c29.44 0 53.333333 23.893333 53.333333 53.333334v53.333333a53.333333 53.333333 0 1 1-106.666666 0v-53.333333c0-29.44 23.893333-53.333333 53.333333-53.333334z"
                    fill="hsl(250deg 69% 61%)"
                    p-id="4125"
                  ></path>
                </svg>
              </a>
            </div>

            <div class="home__img">
              <svg
                class="home__blob"
                viewBox="0 0 200 187"
                xmlns="http://www.w3.org/2000/svg"
                xmlns:xlink="http://www.w3.org/1999/xlink"
              >
                <mask id="mask0" mask-type="alpha">
                  <path
                    d="M190.312 36.4879C206.582 62.1187 201.309 102.826 182.328 134.186C163.346 165.547 
                                130.807 187.559 100.226 186.353C69.6454 185.297 41.0228 161.023 21.7403 129.362C2.45775 
                                97.8511 -7.48481 59.1033 6.67581 34.5279C20.9871 10.1032 59.7028 -0.149132 97.9666 
                                0.00163737C136.23 0.303176 174.193 10.857 190.312 36.4879Z"
                  />
                </mask>
                <g mask="url(#mask0)">
                  <path
                    d="M190.312 36.4879C206.582 62.1187 201.309 102.826 182.328 134.186C163.346 
                                165.547 130.807 187.559 100.226 186.353C69.6454 185.297 41.0228 161.023 21.7403 
                                129.362C2.45775 97.8511 -7.48481 59.1033 6.67581 34.5279C20.9871 10.1032 59.7028 
                                -0.149132 97.9666 0.00163737C136.23 0.303176 174.193 10.857 190.312 36.4879Z"
                  />
                  <image
                    class="home__blob-img"
                    x="0"
                    y="-30"
                    src="@/assets/img/cat.png"
                  />
                </g>
              </svg>
            </div>

            <div class="home__data">
              <h1 class="home__title" i18n="home__title">
                {{ $t("home.opening") }}
              </h1>
              <h3 class="home__subtitle" i18n="home__subtitle">
                {{ $t("home.posts") }}
              </h3>
              <p class="home__description" i18n="home__description">
                {{ $t("home.description") }}
              </p>
              <a href="#contact" class="button button-flex">
                <span i18n="home__contact">{{ $t("home.tips") }}</span>
                <i class="uil uil-message button__icon"></i>
              </a>
            </div>
          </div>
          <div class="home__scroll">
            <a href="#about" class="home__scroll-button button--flex">
              <i class="uil uil-mouse-alt-2 home__scroll-mouse"></i>
              <span class="home__scroll-name" i18n="home__scroll-name">{{
                $t("home.slide")
              }}</span>
              <i class="uil uil-arrow-down home__scroll-arrow"></i>
            </a>
          </div>
        </div>
      </section>

      <!--==================== ABOUT ====================-->
      <section class="about section" id="about">
        <h2 class="section__title" i18n="about__title">
          {{ $t("about.title") }}
        </h2>
        <span class="section__subtitle" i18n="about__subtitle">{{
          $t("about.subheading")
        }}</span>
        <div class="about__container container grid">
          <img src="@/assets/img/about.png" alt="" class="about__img" />

          <div class="about__data">
            <p class="about__description" i18n="about__description">
              {{ $t("about.description") }}
            </p>

            <div class="about__info">
              <div>
                <span class="about__info-title">{{
                  $t("about.number_1")
                }}</span>
                <span class="about__info-name" i18n="about__info-name1">{{
                  $t("about.tips_1")
                }}</span>
              </div>

              <div>
                <span class="about__info-title">{{
                  $t("about.number_2")
                }}</span>
                <span class="about__info-name" i18n="about__info-name2">{{
                  $t("about.tips_2")
                }}</span>
              </div>

              <div>
                <span class="about__info-title">{{
                  $t("about.number_2")
                }}</span>
                <span class="about__info-name" i18n="about__info-name3">{{
                  $t("about.tips_3")
                }}</span>
              </div>
            </div>

            <div class="about__buttons">
              <a download="" class="button button--flex">
                <span i18n="download">{{ $t("about.button") }}</span
                ><i class="uil uil-download-alt button__icon"></i>
              </a>
            </div>
          </div>
        </div>
      </section>

      <!--==================== SKILLS ====================-->
      <section class="skills section" id="skills">
        <h2 class="section__title" i18n="skills__title">
          {{ $t("skills.title") }}
        </h2>
        <span class="section__subtitle" i18n="skills__subtitle">{{
          $t("skills.subheading")
        }}</span>

        <div class="skills__container container grid">
          <div>
            <!--==================== SKILLS 1 ====================-->
            <div class="skills__content" :class="{ skills__open: openSkill1 }">
              <div class="skills__header">
                <i class="uil uil-brackets-curly skills__icon"></i>

                <div>
                  <h1 class="skills__title" i18n="home__subtitle">
                    {{ $t("skills.title_1") }}
                  </h1>
                  <span class="skills__subtitle" i18n="skills__years">{{
                    $t("skills.subheading_1")
                  }}</span>
                </div>

                <i
                  class="uil uil-angle-down skills__arrow"
                  @click="changeSkill1"
                ></i>
              </div>

              <div class="skills__list grid">
                <div class="skills__data">
                  <div class="skills__titles">
                    <h3 class="skills__name">HTML</h3>
                    <span class="skills__number">60%</span>
                  </div>
                  <div class="skills__bar">
                    <div class="skills__percentage skills__html"></div>
                  </div>
                </div>

                <div class="skills__data">
                  <div class="skills__titles">
                    <h3 class="skills__name">CSS</h3>
                    <span class="skills__number">50%</span>
                  </div>
                  <div class="skills__bar">
                    <div class="skills__percentage skills__css"></div>
                  </div>
                </div>

                <div class="skills__data">
                  <div class="skills__titles">
                    <h3 class="skills__name">JavaScript</h3>
                    <span class="skills__number">30%</span>
                  </div>
                  <div class="skills__bar">
                    <div class="skills__percentage skills__js"></div>
                  </div>
                </div>

                <div class="skills__data">
                  <div class="skills__titles">
                    <h3 class="skills__name">VUE</h3>
                    <span class="skills__number">10%</span>
                  </div>
                  <div class="skills__bar">
                    <div class="skills__percentage skills__vue"></div>
                  </div>
                </div>
              </div>
            </div>
            <!--==================== SKILLS 2 ====================-->
            <div class="skills__content" :class="{ skills__open: openSkill2 }">
              <div class="skills__header">
                <i class="uil uil-server-network skills__icon"></i>

                <div>
                  <h1 class="skills__title" i18n="skills__title2">
                    {{ $t("skills.title_2") }}
                  </h1>
                  <span class="skills__subtitle" i18n="skills__years2">{{
                    $t("skills.subheading_2")
                  }}</span>
                </div>

                <i
                  class="uil uil-angle-down skills__arrow"
                  @click="changeSkill2"
                ></i>
              </div>

              <div class="skills__list grid">
                <div class="skills__data">
                  <div class="skills__titles">
                    <h3 class="skills__name">Java</h3>
                    <span class="skills__number">70%</span>
                  </div>
                  <div class="skills__bar">
                    <div class="skills__percentage skills__java"></div>
                  </div>
                </div>

                <div class="skills__data">
                  <div class="skills__titles">
                    <h3 class="skills__name">Python</h3>
                    <span class="skills__number">50%</span>
                  </div>
                  <div class="skills__bar">
                    <div class="skills__percentage skills__python"></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!--==================== QUALIFICATION ====================-->
      <section class="qualification section" id="qualification">
        <h2 class="section__title" i18n="qualification__title">
          {{ $t("qualification.title") }}
        </h2>
        <span class="section__subtitle" i18n="qualification__subtitle">{{
          $t("qualification.subheading")
        }}</span>
        <div class="qualification__container container">
          <div class="qualification__tabs">
            <div
              class="qualification__button button--flex"
              data-target="#education"
              @click="showQualify(1)"
              :class="{ qualification__active: qualifyFlag }"
            >
              <i class="uil uil-graduation-cap qualification__icon"></i>
              <span i18n="education">{{ $t("qualification.title_1") }}</span>
            </div>

            <div
              class="qualification__button button--flex"
              data-target="#work"
              @click="showQualify(2)"
              :class="{ qualification__active: !qualifyFlag }"
            >
              <i class="uil uil-briefcase-alt qualification__icon"></i>
              <span i18n="work">{{ $t("qualification.title_2") }}</span>
            </div>
          </div>
          <div class="qualification__sections">
            <!--==================== QUALIFICATION CONTENT 1 ====================-->
            <div
              class="qualification__content"
              :class="{ qualification__active: qualifyFlag }"
              data-content
              id="education"
            >
              <!--==================== QUALIFICATION 1 ====================-->
              <div class="qualification__data">
                <div>
                  <h3 class="qualification__title" i18n="qualification1__title">
                    {{ $t("qualification.tab_1") }}
                  </h3>
                  <span
                    class="qualification__subtitle"
                    i18n="qualification1__subtitle"
                    >{{ $t("qualification.tab_des_1") }}</span
                  >
                  <div class="qualification__calendar">
                    <i class="uil uil-calendar-alt"></i>
                    2021 - 2025
                  </div>
                </div>

                <div>
                  <span class="qualification__rounder"></span>
                  <span class="qualification__line"></span>
                </div>
              </div>
              <!--==================== QUALIFICATION 2 ====================-->
              <div class="qualification__data">
                <div></div>
                <div>
                  <span class="qualification__rounder"></span>
                  <span class="qualification__line"></span>
                </div>

                <div>
                  <h3 class="qualification__title" i18n="qualification2__title">
                    {{ $t("qualification.tab_2") }}
                  </h3>
                  <span
                    class="qualification__subtitle"
                    i18n="qualification2__subtitle"
                    >{{ $t("qualification.tab_des_2") }}</span
                  >
                  <div class="qualification__calendar">
                    <i class="uil uil-calendar-alt"></i>
                    2021.9 - 2024.6
                  </div>
                </div>
              </div>
              <!--==================== QUALIFICATION 3 ====================-->
              <div class="qualification__data">
                <div>
                  <h3 class="qualification__title" i18n="qualification3__title">
                    {{ $t("qualification.tab_3") }}
                  </h3>
                  <span
                    class="qualification__subtitle"
                    i18n="qualification3__subtitle"
                    >{{ $t("qualification.tab_des_3") }}</span
                  >
                  <div class="qualification__calendar">
                    <i class="uil uil-calendar-alt"></i>
                    2023.9 - ...
                  </div>
                </div>

                <div>
                  <span class="qualification__rounder"></span>
                  <!-- <span class="qualification__line"></span> -->
                </div>
              </div>
            </div>
            <!--==================== QUALIFICATION CONTENT 2 ====================-->
            <div
              class="qualification__content"
              data-content
              id="work"
              :class="{ qualification__active: !qualifyFlag }"
            >
              <!--==================== QUALIFICATION 1 ====================-->
              <div class="qualification__data">
                <div></div>
                <div>
                  <span class="qualification__rounder"></span>
                  <!-- <span class="qualification__line"></span> -->
                </div>
                <div>
                  <h3 class="qualification__title" i18n="qualification4__title">
                    {{ $t("qualification.tab_4") }}
                  </h3>
                  <span
                    class="qualification__subtitle"
                    i18n="qualification4__subtitle"
                    >{{ $t("qualification.tab_des_4") }}</span
                  >
                  <div class="qualification__calendar">
                    <i class="uil uil-calendar-alt"></i>
                    2024
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!--==================== PORTFOLIO ====================-->
      <section class="portfolio section" id="portfolio">
        <h2 class="section__title" i18n="portfolio__title">
          {{ $t("portfolio.title") }}
        </h2>
        <span class="section__subtitle" i18n="portfolio__subtitle">{{
          $t("portfolio.subheading")
        }}</span>

        <div class="portfolio__container container swiper-container">
          <div class="swiper-button-prev" @click="addPage(-1)">
            <i class="uil uil-angle-left-b swiper-portfolio-icon"></i>
          </div>

          <Swiper
            :slides-per-view="1"
            :loop="true"
            @swiper="onSwiper"
            @slideChange="onSlideChange"
          >
            <SwiperSlide>
              <div class="portfolio__content grid">
                <img
                  src="@/assets/img/portfolio1.jpg"
                  alt=""
                  class="portfolio__img"
                />

                <div class="portfolio_">
                  <h3 class="portfolio__title" i18n="portfolio1__title">
                    {{ $t("portfolio.title_1") }}
                  </h3>
                  <p
                    class="portfolio__description"
                    i18n="portfolio1__description"
                  >
                    {{ $t("portfolio.description_1") }}
                  </p>
                  <a
                    href="https://gitee.com/asoutherncat/"
                    target="_blank"
                    class="button button--flex button--small portfolio__button"
                  >
                    Demo
                    <i class="uil uil-arrow-right button__icon"></i>
                  </a>
                </div>
              </div>
            </SwiperSlide>

            <SwiperSlide>
              <div class="portfolio__content grid">
                <img
                  src="@/assets/img/portfolio2.jpg"
                  alt=""
                  class="portfolio__img"
                />

                <div class="portfolio_">
                  <h3 class="portfolio__title" i18n="portfolio2__title">
                    {{ $t("portfolio.title_2") }}
                  </h3>
                  <p
                    class="portfolio__description"
                    i18n="portfolio2__description"
                  >
                    {{ $t("portfolio.description_2") }}
                  </p>
                  <a
                    href="https://gitee.com/asoutherncat/"
                    target="_blank"
                    class="button button--flex button--small portfolio__button"
                  >
                    Demo
                    <i class="uil uil-arrow-right button__icon"></i>
                  </a>
                </div>
              </div>
            </SwiperSlide>

            <SwiperSlide>
              <div class="portfolio__content grid">
                <img
                  src="@/assets/img/portfolio3.jpg"
                  alt=""
                  class="portfolio__img"
                />

                <div class="portfolio_">
                  <h3 class="portfolio__title" i18n="portfolio3__title">
                    {{ $t("portfolio.title_3") }}
                  </h3>
                  <p
                    class="portfolio__description"
                    i18n="portfolio3__description"
                  >
                    {{ $t("portfolio.description_3") }}
                  </p>
                  <a
                    href="https://gitee.com/asoutherncat/"
                    target="_blank"
                    class="button button--flex button--small portfolio__button"
                  >
                    Demo
                    <i class="uil uil-arrow-right button__icon"></i>
                  </a>
                </div>
              </div>
            </SwiperSlide>
          </Swiper>
          <!-- add arrows-->
          <div class="swiper-button-next" @click="addPage(1)">
            <i class="uil uil-angle-right-b swiper-portfolio-icon"></i>
          </div>

          <!-- Add Pagination -->
          <div class="swiper-pagination"></div>
        </div>
      </section>

      <!--==================== CONTACT ME ====================-->
      <section class="contact section" id="contact">
        <h2 class="section__title" i18n="contact__title">
          {{ $t("contact.title") }}
        </h2>
        <span class="section__subtitle" i18n="contact__subtitle">{{
          $t("contact.subheading")
        }}</span>

        <div class="contact__container container grid">
          <div>
            <div class="contact__information">
              <i class="uil uil-phone-alt contact__icon"></i>

              <div>
                <h3 class="contact__title" i18n="tel">
                  {{ $t("contact.phone") }}
                </h3>
                <span class="contact__subtitle" i18n="tel__number"
                  >166********</span
                >
              </div>
            </div>

            <div class="contact__information">
              <i class="uil uil-envelope contact__icon"></i>

              <div>
                <h3 class="contact__title" i18n="email">
                  {{ $t("contact.email") }}
                </h3>
                <span class="contatc__subtitle" i18n="email__address"
                  >asoutherncat@gmail.com</span
                >
              </div>
            </div>

            <div class="contact__information">
              <i class="uil uil-map-marker contact__icon"></i>

              <div>
                <h3 class="contact__title" i18n="location">
                  {{ $t("contact.position") }}
                </h3>
                <span class="contatc__subtitle" i18n="location__detail">{{
                  $t("contact.location")
                }}</span>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>

    <!--==================== FOOTER ====================-->
    <footer class="footer">
      <div class="footer__bg">
        <div class="footer__container container grid">
          <div>
            <h1 class="footer__title">Singor</h1>
            <span class="footer__subtitle" i18n="home__subtitle">{{
              $t("footer.tab_1")
            }}</span>
          </div>

          <ul class="footer__links">
            <li>
              <a href="#skills" class="footer__link" i18n="skills">{{
                $t("footer.tab_2")
              }}</a>
            </li>
            <li>
              <a href="#qualification" class="footer__link" i18n="portfolio">{{
                $t("footer.tab_3")
              }}</a>
            </li>
            <li>
              <a href="#portfolio" class="footer__link" i18n="contact">{{
                $t("footer.tab_4")
              }}</a>
            </li>
          </ul>

          <div class="footer__socials">
            <a href="https://gitee.com/asoutherncat/" target="_blank">
              <!-- <i class="uil uil-linkedin-alt"></i> -->
              <svg
                class="icon footer__social"
                aria-hidden="true"
                t="1674798484640"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="2757"
              >
                <path
                  d="M512 1024C229.222 1024 0 794.778 0 512S229.222 0 512 0s512 229.222 512 512-229.222 512-512 512z m259.149-568.883h-290.74a25.293 25.293 0 0 0-25.292 25.293l-0.026 63.206c0 13.952 11.315 25.293 25.267 25.293h177.024c13.978 0 25.293 11.315 25.293 25.267v12.646a75.853 75.853 0 0 1-75.853 75.853h-240.23a25.293 25.293 0 0 1-25.267-25.293V417.203a75.853 75.853 0 0 1 75.827-75.853h353.946a25.293 25.293 0 0 0 25.267-25.292l0.077-63.207a25.293 25.293 0 0 0-25.268-25.293H417.152a189.62 189.62 0 0 0-189.62 189.645V771.15c0 13.977 11.316 25.293 25.294 25.293h372.94a170.65 170.65 0 0 0 170.65-170.65V480.384a25.293 25.293 0 0 0-25.293-25.267z"
                  fill="#ffffff"
                  p-id="2758"
                ></path>
              </svg>
            </a>

            <a href="https://t.bilibili.com/" target="_blank">
              <!-- <i class="uil uil-dribbble"></i> -->
              <svg
                class="icon footer__social"
                aria-hidden="true"
                t="1674801734735"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="4124"
              >
                <path
                  d="M777.514667 131.669333a53.333333 53.333333 0 0 1 0 75.434667L728.746667 255.829333h49.92A160 160 0 0 1 938.666667 415.872v320a160 160 0 0 1-160 160H245.333333A160 160 0 0 1 85.333333 735.872v-320a160 160 0 0 1 160-160h49.749334L246.4 207.146667a53.333333 53.333333 0 1 1 75.392-75.434667l113.152 113.152c3.370667 3.370667 6.186667 7.04 8.448 10.965333h137.088c2.261333-3.925333 5.12-7.68 8.490667-11.008l113.109333-113.152a53.333333 53.333333 0 0 1 75.434667 0z m1.152 231.253334H245.333333a53.333333 53.333333 0 0 0-53.205333 49.365333l-0.128 4.010667v320c0 28.117333 21.76 51.157333 49.365333 53.162666l3.968 0.170667h533.333334a53.333333 53.333333 0 0 0 53.205333-49.365333l0.128-3.968v-320c0-29.44-23.893333-53.333333-53.333333-53.333334z m-426.666667 106.666666c29.44 0 53.333333 23.893333 53.333333 53.333334v53.333333a53.333333 53.333333 0 1 1-106.666666 0v-53.333333c0-29.44 23.893333-53.333333 53.333333-53.333334z m320 0c29.44 0 53.333333 23.893333 53.333333 53.333334v53.333333a53.333333 53.333333 0 1 1-106.666666 0v-53.333333c0-29.44 23.893333-53.333333 53.333333-53.333334z"
                  fill="#ffffff"
                  p-id="4125"
                ></path>
              </svg>
            </a>
          </div>
        </div>

        <p class="footer__copy">
          &#169; Singor.Copy from
          <a
            class="footer__copy"
            href="https://www.youtube.com/c/Bedimcode"
            target="_blank"
            >Bedimcode</a
          >
        </p>
      </div>
    </footer>

    <!--==================== SCROLL TOP ====================-->
    <a
      href="#"
      class="scrollup"
      :class="{ 'show-scroll': showScroll }"
      id="scroll-up"
    >
      <i class="uil uil-arrow-up scrollup__icon"></i>
    </a>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref, getCurrentInstance, watch } from "vue";
import { useI18n } from "vue-i18n";
// import "./swiper/swiper-bundle.min.js";
// import "./swiper/swiper-bundle.min.css";
import { Swiper, SwiperSlide } from "swiper/vue";
import "swiper/css";
const { t } = useI18n();
const { proxy } = getCurrentInstance();

//用于判断头部阴影是否显示
let isScrolled = ref(false);
// 用于检测滚动并激活链接的响应式引用
let activeSection = ref(null);
//用于判断当前是否暗色主题
let isDarkTheme = ref(false);
//用于切换主题按钮的图标
let themeIcon = ref("uil-moon");
//用于判断是否显示按钮组
let showmenu = ref(false);
//用于判断是否显示回到顶部按钮
let showScroll = ref(false);
//用于判断skill界面是否展开
let openSkill1 = ref(true);
let openSkill2 = ref(false);
//用于判断显示哪个技能
let qualifyFlag = ref(true);
let useSwiper = ref(null);

//用于判断标签
const sections = ref([
  { id: "home", icon: "uil-estate", label: "nav.home" },
  { id: "about", icon: "uil-user", label: "nav.about" },
  { id: "skills", icon: "uil-file-alt", label: "nav.skills" },
  { id: "qualification", icon: "uil-file-alt", label: "nav.qualification" },
  { id: "portfolio", icon: "uil-scenery", label: "nav.portfolio" },
  { id: "contact", icon: "uil-message", label: "nav.contact" },
]);

//切换语言
const changeLang = () => {
  let lang = localStorage.getItem("localeLang");
  let value = null;
  if (lang === null) {
    lang = "zhCn";
    value = lang;
  } else {
    value = lang === "zhCn" ? "en" : "zhCn";
  }
  localStorage.setItem("localeLang", value);
  proxy.$i18n.locale = value;
};

//切换主题
const changeTheme = () => {
  isDarkTheme.value = !isDarkTheme.value;
  themeIcon.value = isDarkTheme.value ? "uil-sun" : "uil-moon";
  document.body.classList.toggle("dark-theme");
};

// 监听isDarkTheme的变化，并更新localStorage
const saveTheme = () => {
  localStorage.setItem("selected-theme", isDarkTheme.value ? "dark" : "light");
};

//用于判断是否显示菜单
const showMenu = () => {
  showmenu.value = !showmenu.value;
  console.log(showmenu.value);
};

// 检查给定ID的section是否处于激活状态
const isActive = (id) => {
  return id === activeSection.value;
};

// 展示skill1界面
const changeSkill1 = () => {
  openSkill1.value = !openSkill1.value;
};

// 展示skill2界面
const changeSkill2 = () => {
  openSkill2.value = !openSkill2.value;
};

//用于切换展示的经历
const showQualify = (value) => {
  qualifyFlag.value = value === 1 ? true : false;
};

const onSwiper = (swiper) => {
  useSwiper.value = swiper;
};

const onSlideChange = () => {};

const addPage = (value) => {
  if (value > 0) {
    useSwiper.value.slideNext();
  } else if (value < 0) {
    useSwiper.value.slidePrev();
  }
};

// 处理滚动事件
const handleScroll = () => {
  isScrolled.value = window.scrollY >= 80;

  showScroll.value = window.scrollY >= 560 ? true : false;

  const sectionsInView = Array.from(
    document.querySelectorAll(".section")
  ).filter((section) => {
    // console.log(
    //   section.id,
    //   " ",
    //   section.getBoundingClientRect().top,
    //   " ",
    //   section.getBoundingClientRect().bottom
    // );
    return (
      section.getBoundingClientRect().top <= window.innerHeight &&
      section.getBoundingClientRect().bottom > 1
    );
  });
  if (sectionsInView.length > 0) {
    activeSection.value = sectionsInView[0].id; // 只激活最上面的可见部分
  } else {
    activeSection.value = null;
  }
};

// 监听isDarkTheme的变化
watch(() => isDarkTheme.value, saveTheme);

// 组件挂载后添加滚动监听器
onMounted(() => {
  window.addEventListener("scroll", handleScroll);

  // 从localStorage获取之前的主题设置
  const storedTheme = localStorage.getItem("selected-theme");
  if (storedTheme) {
    isDarkTheme.value = storedTheme === "dark";
    themeIcon.value = isDarkTheme.value ? "uil-sun" : "uil-moon";
  }
  document.body.classList[isDarkTheme.value ? "add" : "remove"]("dark-theme");
});

// 组件卸载前移除滚动监听器，防止内存泄漏
onUnmounted(() => {
  window.removeEventListener("scroll", handleScroll);
});
</script>

<style lang="scss">
@import "./style.scss";
</style>
