import { createApp, defineAsyncComponent } from 'vue'

const modules = import.meta.glob('./**/**.vue')

const components = {}

const keyList = Object.keys(modules)

keyList.forEach((key) => {
    const component = defineAsyncComponent(modules[key])
    let name = key.replace(/(\.\/|\.vue|\.js)/g, '')
    //保留字符串中最后一个/之后的字符串
    const lastSlashIndex = name.lastIndexOf('/')
    if (lastSlashIndex !== -1){
        name=name.slice(lastSlashIndex + 1)
    }
    console.log(name)
    components[name] = component
});

export default components;
