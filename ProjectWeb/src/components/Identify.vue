<template>
  <div class="canvas-box" @click="refreshCode" :style="{ height: contentHeight + 'px' }">
    <canvas id="id-canvas" class="id-canvas" :width="contentWidth" :height="contentHeight"></canvas>
  </div>
</template>

<script setup>
import {onMounted, ref, watch,defineEmits,defineExpose} from 'vue';

const emit = defineEmits(['identifyCode'])

let identifyCode=ref("1234")

const props = defineProps({
  fontSizeMin: {
    type: Number,
    default: 25
  },
  fontSizeMax: {
    type: Number,
    default: 35
  },
  backgroundColorMin: {
    type: Number,
    default: 200
  },
  backgroundColorMax: {
    type: Number,
    default: 220
  },
  dotColorMin: {
    type: Number,
    default: 60
  },
  dotColorMax: {
    type: Number,
    default: 120
  },
  contentWidth: {
    type: Number,
    default: 100
  },
  contentHeight: {
    type: Number,
    default: 40
  }
});

const randomNum = (min, max) => {
  return Math.floor(Math.random() * (max - min) + min);
};

const randomColor = (min, max) => {
  const r = randomNum(min, max);
  const g = randomNum(min, max);
  const b = randomNum(min, max);
  return `rgb(${r},${g},${b})`;
};

const drawText = (ctx, txt, i) => {
  ctx.fillStyle = randomColor(50, 160);
  ctx.font = `${randomNum(props.fontSizeMin, props.fontSizeMax)}px SimHei`;
  const x = (i + 1) * (props.contentWidth / (identifyCode.value.length + 1));
  const y = randomNum(props.fontSizeMax, props.contentHeight - 5);
  const deg = randomNum(-30, 30);

  ctx.translate(x, y);
  ctx.rotate((deg * Math.PI) / 180);
  ctx.fillText(txt, 0, 0);
  ctx.rotate((-deg * Math.PI) / 180);
  ctx.translate(-x, -y);
};

const drawLine = (ctx) => {
  for (let i = 0; i < 4; i++) {
    ctx.strokeStyle = randomColor(100, 200);
    ctx.beginPath();
    ctx.moveTo(randomNum(0, props.contentWidth), randomNum(0, props.contentHeight));
    ctx.lineTo(randomNum(0, props.contentWidth), randomNum(0, props.contentHeight));
    ctx.stroke();
  }
};

const drawDot = (ctx) => {
  for (let i = 0; i < 30; i++) {
    ctx.fillStyle = randomColor(0, 255);
    ctx.beginPath();
    ctx.arc(randomNum(0, props.contentWidth), randomNum(0, props.contentHeight), 1, 0, 2 * Math.PI);
    ctx.fill();
  }
};

const drawPic = () => {
  const canvas = document.getElementById('id-canvas');
  const ctx = canvas.getContext('2d');
  ctx.textBaseline = 'bottom';

  ctx.fillStyle = '#e6ecfd';
  ctx.fillRect(0, 0, props.contentWidth, props.contentHeight);

  for (let i = 0; i < identifyCode.value.length; i++) {
    drawText(ctx, identifyCode.value[i], i);
  }

  drawLine(ctx);
  drawDot(ctx);
};

const refreshCode = () => {
  const identifyCodes = "1234567890abcdefjhijklinopqrsduvwxyz";
  identifyCode.value = '';
  for (let i = 0; i < 4; i++) {
    identifyCode.value += identifyCodes[randomNum(0, identifyCodes.length)];
  }
  emit('identifyCode', identifyCode.value);
};

onMounted(() => {
  refreshCode()
  drawPic();
});

watch(() => identifyCode.value, (val) => {
  drawPic();
});

defineExpose({refreshCode})
</script>

<style scoped>

.canvas-box {
  cursor: pointer;
  .id-canvas {
    height: 100%;
  }
}
</style>