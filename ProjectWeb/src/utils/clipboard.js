// 复制工具类


/**
 * 输入框复制文本事件回调(将复制带样式的文本样式清空, 只保留纯文本)
 * @param {*} e 
 */
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
