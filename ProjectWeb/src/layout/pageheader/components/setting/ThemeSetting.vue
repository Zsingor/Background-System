<template>
  <div>
    <el-divider content-position="left">
      <h3>{{ $t('themeSetting.title_1') }}</h3>
    </el-divider>
    <div class="theme-container">
      <div class="theme-container-item" @click="setLayoutTheme('theme-1')">
        <el-container>
          <el-header height="20px" style="background: #16aad8" />
          <el-container>
            <el-aside width="40px" style="height: 100px; background: #545c64" />
            <el-main style="background: #f8f8f8"></el-main>
          </el-container>
        </el-container>
      </div>
      <div class="theme-container-item" @click="setLayoutTheme('theme-2')">
        <el-container>
          <el-header height="20px" style="background: #5d28e9" />
          <el-container>
            <el-aside width="40px" style="height: 100px; background: #ebebeb" />
            <el-main style="background: #f8f8f8"></el-main>
          </el-container>
        </el-container>
      </div>
      <div class="theme-container-item" @click="setLayoutTheme('theme-3')">
        <el-container>
          <el-header height="20px" style="background: hsl(160, 9%, 94%)" />
          <el-container>
            <el-aside width="40px" style="height: 100px; background: #e5e5e5" />
            <el-main style="background: #f8f8f8"></el-main>
          </el-container>
        </el-container>
      </div>
      <div class="theme-container-item" @click="setLayoutTheme('theme-4')">
        <el-container>
          <el-header height="20px" style="background: hsl(61, 0%, 19%)" />
          <el-container>
            <el-aside width="40px" style="height: 100px; background: #3c3f41" />
            <el-main style="background: #f8f8f8"></el-main>
          </el-container>
        </el-container>
      </div>
    </div>
    <el-divider content-position="left">
      <h3>{{ $t('themeSetting.title_2') }}</h3>
    </el-divider>
    <div class="theme-item">
      <p>{{ $t('themeSetting.theme_1') }}</p>
      <el-color-picker
        size="default"
        color-format="hsl"
        v-model="persistentConfig.theme.header.bgColor"
        @active-change="updateHeader"
      />
    </div>
    <div class="theme-item">
      <p>{{ $t('themeSetting.theme_2') }}</p>
      <el-color-picker
        size="default"
        v-model="persistentConfig.theme.header.nprogressColor"
        @active-change="setNprogressColor"
      />
    </div>
    <div class="theme-item">
      <p>{{ $t('themeSetting.theme_3') }}</p>
      <el-color-picker
        size="default"
        color-format="hsl"
        v-model="persistentConfig.theme.tag.bgColor"
        @active-change="updateTag"
      />
    </div>
    <div class="theme-item">
      <p>{{ $t('themeSetting.theme_4') }}</p>
      <el-color-picker
        size="default"
        color-format="hsl"
        v-model="persistentConfig.theme.tag.activeFontColor"
        @active-change="updateTagFontColor"
      />
    </div>
    <div class="theme-item">
      <p>{{ $t('themeSetting.theme_5') }}</p>
      <el-color-picker
        size="default"
        v-model="persistentConfig.theme.aside.bgColor"
        @active-change="updateAside"
      />
    </div>
    <div class="theme-item">
      <p>{{ $t('themeSetting.theme_6') }}</p>
      <el-color-picker
        size="default"
        v-model="persistentConfig.theme.aside.fontColor"
        @active-change="updateAsideFontColor"
      />
    </div>
    <div class="theme-item">
      <p>{{ $t('themeSetting.theme_7') }}</p>
      <el-color-picker
        size="default"
        v-model="persistentConfig.theme.aside.fontActiveColor"
        @active-change="updateAsideFontActiveColor"
      />
    </div>
    <div class="theme-item">
      <p>{{ $t('themeSetting.theme_8') }}</p>
      <el-color-picker
        size="default"
        v-model="persistentConfig.theme.aside.backgroundColor"
        @active-change="updateAsideBgActiveColor"
      />
    </div>
    <el-button
      style="width: 50%; margin-left: 25%; margin-top: 8px; margin-bottom: 8px"
      size="default"
      type="info"
      @click="
        () => {
          persistentConfig.theme.header = _.cloneDeep(defaultTheme.header)
          persistentConfig.theme.tag = _.cloneDeep(defaultTheme.tag)
          persistentConfig.theme.aside = _.cloneDeep(defaultTheme.aside)
        }
      "
    >
      {{ $t('themeSetting.reset') }}
    </el-button>
    <el-divider content-position="left">
      <h3>{{ $t('themeSetting.title_3') }}</h3>
    </el-divider>
    <div class="theme-item">
      <p>Primary：</p>
      <el-color-picker
        size="default"
        v-model="persistentConfig.theme.global.primary"
        @active-change="updatePrimaryTheme"
      />
    </div>
    <div class="theme-item">
      <p>Info：</p>
      <el-color-picker
        size="default"
        v-model="persistentConfig.theme.global.info"
        @active-change="updateInfoTheme"
      />
    </div>
    <div class="theme-item">
      <p>Success：</p>
      <el-color-picker
        size="default"
        v-model="persistentConfig.theme.global.success"
        @active-change="updateSuccessTheme"
      />
    </div>
    <div class="theme-item">
      <p>Warning：</p>
      <el-color-picker
        size="default"
        v-model="persistentConfig.theme.global.warning"
        @active-change="updateWarningTheme"
      />
    </div>
    <div class="theme-item">
      <p>Error：</p>
      <el-color-picker
        size="default"
        v-model="persistentConfig.theme.global.error"
        @active-change="updateErrorTheme"
      />
    </div>
    <el-button
      style="width: 50%; margin-left: 25%; margin-top: 8px; margin-bottom: 8px"
      size="default"
      type="info"
      @click="resetthemedefault"
    >
      {{ $t('themeSetting.reset') }}
    </el-button>
  </div>
</template>

<script setup>
import _ from 'lodash'
import {
  persistentConfig,
  defaultTheme,
  updatePrimaryTheme,
  updateInfoTheme,
  updateSuccessTheme,
  updateWarningTheme,
  updateErrorTheme,
  setNprogressColor,
} from '@/layout/layout.js'

const setLayoutTheme = (type) => {
  switch (type) {
    case 'theme-1':
      updateHeader('#16aad8')
      updateTag('#16aad8')
      updateAside('#545c64')
      setNprogressColor('#dc2626')
      persistentConfig.theme.aside.fontColor = '#ffffff'
      persistentConfig.theme.aside.fontActiveColor = '#ffd04b'
      persistentConfig.theme.aside.backgroundColor = '#337DBE'
      break
    case 'theme-2':
      updateHeader('#5D29E5')
      updateTag('hsl(241, 70%, 67%)')
      updateAside('#EBEBEB')
      setNprogressColor('#dc2626')
      persistentConfig.theme.aside.fontColor = '#3D3737'
      persistentConfig.theme.aside.fontActiveColor = '#4700FF'
      persistentConfig.theme.aside.backgroundColor = '#D9D9D9'
      break
    case 'theme-3':
      updateHeader('hsl(160,10%,95%)')
      updateTag('hsl(200,57%,50%)')
      updateAside('#E5E5E5')
      setNprogressColor('#16aad8')
      persistentConfig.theme.aside.fontColor = '#171616'
      persistentConfig.theme.aside.fontActiveColor = '#ffffff'
      persistentConfig.theme.aside.backgroundColor = '#2C486D'
      break
    case 'theme-4':
      updateHeader('hsl(61, 0%, 19%)')
      updateTag('hsl(61, 0%, 19%)')
      updateAside('#3c3f41')
      setNprogressColor('#16aad8')
      persistentConfig.theme.aside.fontColor = '#DBDBDB'
      persistentConfig.theme.aside.fontActiveColor = '#ffffff'
      persistentConfig.theme.aside.backgroundColor = '#5178F9'
  }
}

//恢复按钮主题色的默认配置
const resetthemedefault = () => {
  persistentConfig.theme.global = _.cloneDeep(defaultTheme.global)
  updatePrimaryTheme(persistentConfig.theme.global.primary)
  updateInfoTheme(persistentConfig.theme.global.info)
  updateSuccessTheme(persistentConfig.theme.global.success)
  updateErrorTheme(persistentConfig.theme.global.error)
  updateWarningTheme(persistentConfig.theme.global.warning)
}

// 更新导航头主题
const updateHeader = (value) => {
  const hue = Number(
    value.substring(value.indexOf('(') + 1, value.indexOf(','))
  )
  const saturation = Number(
    value.substring(value.indexOf(',') + 1, value.indexOf('%'))
  )
  const lightness = Number(
    value.substring(value.lastIndexOf(',') + 1, value.lastIndexOf('%'))
  )
  let lightness1
  let lightness2
  let fontColor
  persistentConfig.theme.header.bgColor = value
  if (lightness < 70) {
    lightness1 = lightness + 15
    lightness2 = lightness + 30
    fontColor = `hsl(${hue},${saturation}%,100%)`
  } else {
    lightness1 = lightness - 20
    lightness2 = lightness - 40
    fontColor = `hsl(${hue},${saturation}%,${lightness - 70}%)`
  }
  persistentConfig.theme.header.bgColor1 = `hsl(${hue},${saturation}%,${lightness1}%)`
  persistentConfig.theme.header.bgColor2 = `hsl(${hue},${saturation}%,${lightness2}%)`
  persistentConfig.theme.header.fontColor = fontColor
}

// 更新标签导航主题
const updateTag = (value) => {
  const hue = Number(
    value.substring(value.indexOf('(') + 1, value.indexOf(','))
  )
  const saturation = Number(
    value.substring(value.indexOf(',') + 1, value.indexOf('%'))
  )
  const lightness = Number(
    value.substring(value.lastIndexOf(',') + 1, value.lastIndexOf('%'))
  )
  let lightness1
  let lightness2
  let fontColor
  persistentConfig.theme.tag.bgColor = value
  if (lightness < 70) {
    lightness1 = lightness + 15
    lightness2 = lightness + 30
    fontColor = `hsl(${hue},${saturation}%,100%)`
  } else {
    lightness1 = lightness - 20
    lightness2 = lightness - 40
    fontColor = `hsl(${hue},${saturation}%,${lightness - 70}%)`
  }
  persistentConfig.theme.tag.bgColor1 = `hsl(${hue},${saturation}%,${lightness1}%)`
  persistentConfig.theme.tag.bgColor2 = `hsl(${hue},${saturation}%,${lightness2}%)`
  persistentConfig.theme.tag.fontColor = fontColor
}

const updateTagFontColor = (value) => {
  persistentConfig.theme.tag.activeFontColor = value
}

// 更新侧边栏背景
const updateAside = (value) => {
  persistentConfig.theme.aside.bgColor = value
}

// 更新侧边栏文字颜色
const updateAsideFontColor = (value) => {
  persistentConfig.theme.aside.fontColor = value
}

// 更新侧边栏文字激活颜色
const updateAsideFontActiveColor = (value) => {
  persistentConfig.theme.aside.fontActiveColor = value
}

const updateAsideBgActiveColor = (value) => {
  persistentConfig.theme.aside.backgroundColor = value
}
</script>

<style scoped>
.theme-container {
  display: flex;
  flex-wrap: wrap;

  .theme-container-item {
    width: 50%;
    height: 130px;
    padding: 8px;

    &:hover {
      cursor: pointer;
    }
  }
}

.theme-item {
  width: 100%;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;

  p {
    color: #545c64;
  }
}
</style>
