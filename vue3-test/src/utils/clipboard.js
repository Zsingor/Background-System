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
