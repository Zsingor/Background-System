<template>
  <div>
    <el-divider content-position="left">
      <h3>{{ $t('baseSetting.title_1') }}</h3>
    </el-divider>
    <el-form label-width="140px" label-position="left" size="default">
      <el-form-item class="fold_label" :label="$t('baseSetting.cache')">
        <el-switch v-model="persistentConfig.openKeepalive" />
      </el-form-item>
      <el-form-item class="fold_label" :label="$t('baseSetting.watermark')">
        <el-switch v-model="persistentConfig.closeWaterMark" />
      </el-form-item>
      <el-form-item
        class="fold_label"
        :label="$t('baseSetting.language')"
      >
        <el-select v-model="persistentConfig.localeLang">
          <el-option
            v-for="lang in langOption"
            :key="lang.value"
            :label="lang.label"
            :value="lang.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        class="fold_label err_label"
        :label="$t('baseSetting.pagination')"
        :error="$t('baseSetting.errorTips')"
      >
        <el-select v-model="persistentConfig.defaultPageSize">
          <el-option
            v-for="size in pageSizes"
            :key="size"
            :label="size"
            :value="size"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="fold_label" :label="$t('baseSetting.drawer')">
        <el-select v-model="persistentConfig.drawerPosition">
          <el-option
            v-for="position in drawerPositions"
            :key="position.value"
            :label="position.label"
            :value="position.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="fold_label" :label="$t('baseSetting.notification')">
        <el-select v-model="persistentConfig.notiPosition">
          <el-option
            v-for="position in notiPositions"
            :key="position.value"
            :label="position.label"
            :value="position.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <el-divider content-position="left">
      <h3>{{ $t('baseSetting.title_2') }}</h3>
    </el-divider>
    <el-form label-width="140px" label-position="left" size="default">
      <el-form-item class="fold_label" :label="$t('baseSetting.scroll')">
        <el-switch
          size="default"
          v-model="persistentConfig.scroll.openCustom"
        />
      </el-form-item>
      <el-form-item
        class="fold_label err_label"
        :label="$t('baseSetting.size')"
        :error="$t('baseSetting.errorTips_2')"
      >
        <el-select v-model="persistentConfig.scroll.size" size="default">
          <el-option
            v-for="size in scrollSizes"
            :key="size.value"
            :label="size.label"
            :value="size.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { persistentConfig } from '@/layout/layout.js'
import { pageSizes } from '@/utils/VxeTableConfig.js'

const drawerPositions = [
  {
    label: '左边',
    value: 'ltr',
  },
  {
    label: '右边',
    value: 'rtl',
  },
  {
    label: '上面',
    value: 'ttb',
  },
  {
    label: '下面',
    value: 'btt',
  },
]

const notiPositions = [
  {
    label: '右上方',
    value: 'top-right',
  },
  {
    label: '右下方',
    value: 'bottom-right',
  },
  {
    label: '左上方',
    value: 'top-left',
  },
  {
    label: '左下方',
    value: 'bottom-left',
  },
]

const scrollSizes = [
  {
    label: '偏大',
    value: 'large',
  },
  {
    label: '中等',
    value: 'medium',
  },
  {
    label: '偏小',
    value: 'small',
  },
  {
    label: '隐藏',
    value: 'hide',
  },
]

const langOption = [
  {
    label: '简体中文(zhCn)',
    value: 'zhCn',
  },
  {
    label: 'English(en)',
    value: 'en',
  },
]
</script>

<style scoped>
.global-setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.label {
  font-weight: 400;
  font-size: 14px;
  color: #525252;
}

.fold_label :deep(.el-form-item__label){
  white-space: pre-line;
  line-height: 20px;
}

.err_label :deep(.el-form-item__content){
  margin-bottom: 30px;
}
</style>