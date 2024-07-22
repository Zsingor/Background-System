<template>
  <div>
    <div class="container">
      <img class="logo" src="@/assets/img.png" alt="logo">
      <div class="square blue"></div>
      <div class="square purple"></div>
      <div class="square red"></div>
      <div class="square orange"></div>
    </div>
  </div>
</template>

<script setup>

import {onMounted} from "vue";
import {gsap} from 'gsap'

onMounted(()=>{
  //gsap.to('.logo',{duration:3,x:300,scale:1.5,rotation:360})
  const TL=gsap.timeline({repeat:-1,repeatDelay:1,yoyo:true})
  TL.from('.square',{y:150,stagger:0.5,duration:1,opacity:0})
  TL.to('.blue',{rotation:360})
  TL.to('.purple',{rotation:360})
  TL.to('.red',{rotation:360})
  TL.to('.orange',{rotation:360})
})

</script>

<style scoped lang="scss">
:root{
  --blue:#2c7ad2;
  --purple:#8d3dae;
  --red:#c82736;
  --orange:#e77614;
}

.container{
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 0 50px;
  height: 100vh;
  .logo{
    width: 120px;
    aspect-ratio: 1/1;
  }

  .square{
    width: 75px;
    height: 75px;
    border-radius: 12px;
    //background-color: var(--blue);
    &:nth-child(2){
      background-color: #2c7ad2;
    }
    &:nth-child(3){
      background: #8d3dae;
    }
    &:nth-child(4){
      background: #c82736;
    }
    &:nth-child(5){
      background: #e77614;
    }
  }

}
</style>
