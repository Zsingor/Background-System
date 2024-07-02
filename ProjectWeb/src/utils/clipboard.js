// 复制工具类

import Clipboard from 'clipboard'
import {message} from "@/utils/message.js";


function clipboardSuccess() {
  message('复制成功')
}

function clipboardError() {
  message('复制失败','error')
}

export default function handleClipboard(text, event) {
  const clipboard = new Clipboard(event.target, {
    text: () => text
  })
  clipboard.on('success', () => {
    clipboardSuccess()
    clipboard.destroy()
  })
  clipboard.on('error', () => {
    clipboardError()
    clipboard.destroy()
  })
  clipboard.onClick(event)
}


// 输入框复制文本事件回调(将复制带样式的文本样式清空, 只保留纯文本)
export const handlePaste = (e) => {
  e.preventDefault()
  let text
  let clp = (e.originalEvent || e).clipboardData
  if (clp === undefined || clp === null) {
    text = window.clipboardData.getData("text") || ""
    if (text !== "") {
      if (window.getSelection) {
        let newNode = document.createElement("span")
        newNode.innerHTML = text;
        window.getSelection().getRangeAt(0).insertNode(newNode)
      } else {
        document.selection.createRange().pasteHTML(text)
      }
    }
  } else {
    text = clp.getData('text/plain') || ""
    if (text !== "") {
      document.execCommand('insertText', false, text)
    }
  }
}
