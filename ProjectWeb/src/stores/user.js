import { defineStore } from "pinia"
import {Names} from './store-name.js'

export const useUserInfoStore = defineStore(Names.Test, {
    // defineStore('userInfo',{})  userInfo就是这个仓库的名称name
    state: () => ({
        username: '名字',
        age: 30,
        like: 'boy',
        obj:{ money:100,friend:10 },
        hobby: [
            { id: 1, name: '篮球', level: 1 },
            { id: 2, name: 'rap', level: 10 }
        ]
    }),
    //相当于computeed
    getters:{
        getName(state){
            return `$-${state.username}`
        }
    },
    //相当于methods
    actions:{
        //不用箭头函数是因为要使用this
        setAge(value){
            this.age=value
        }
    },
    persist: {
        key: 'piniaStore', //存储名称
        storage: sessionStorage, // 存储方式 sessionStorage/localStorage
        paths: null, //指定 state 中哪些数据需要被持久化。[] 表示不持久化任何状态，undefined 或 null 表示持久化整个 state
    },
})
