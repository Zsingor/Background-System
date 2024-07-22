import {ElMessage} from "element-plus";

/**
 * 基于element-plus通用的消息提示函数
 * @param text
 * @param type
 */
export function message(text, type) {
    ElMessage({
        message: text,
        type: type || "success",
        duration: 2000,
        showClose: true
    });
}

const showMessage = Symbol('showMessage')
/**
 * 封装element-plus message方法，只有在页面没有message或者没有相同message的情况下才弹出该message
 */
class OnceMessage {
    success(options, single = true) {
        this[showMessage]('success', options, single)
    }

    warning(options, single = true) {
        this[showMessage]('warning', options, single)
    }

    info(options, single = true) {
        this[showMessage]('info', options, single)
    }

    error(options, single = true) {
        this[showMessage]('error', options, single)
    }
    /* eslint-disable */
    [showMessage] (type, options, single) {
        if (single) {
            //获取页面所有已经存在的message
            let doms = document.getElementsByClassName('el-message');
            //el-message__content
            //设置值控制显示当前message
            let canShow = true;
            //遍历获取到的message DOM集合
            for( let i=0; i<doms.length; i++){
                //如果页面已存在的message中有显示文本和当前message相同的情况，canShow设置为false
                if(options === doms[i].getElementsByClassName('el-message__content')[0].innerHTML){
                    canShow = false;
                }
            }
            //如果页面不存在message或者canShow==true，则正常执行该message
            if(doms.length === 0 || canShow){
                ElMessage[type](options)
            }
        } else {
            ElMessage[type](options)
        }
    }
}
const onceMessage=new OnceMessage()
export {
    onceMessage
}
