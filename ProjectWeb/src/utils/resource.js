//静态资源工具类


/**
 * 访问文件在线地址
 * @param fileName  要访问的文件名
 */
export const getImageUrl=(fileName)=>{
    return import.meta.env.VITE_BASE_API+'/resource/images/' + fileName;
}

/**
 * 下载本地文件
 * @param {*} path 本地文件路径，注意：必须保存在public文件夹下
 * @param {*} name 下载后的文件名
 */
export const downloadLocalTemplate = (path, name) => {
    let a = document.createElement('a');
    a.href = path;  // 如果后端返回文件地址，path值就是后端返回的地址
    a.download = name; // 设置下载文件文件名,要完整的文件名+后缀，比如：test.xlsx
    a.style.display = 'none';
    document.body.appendChild(a);
    a.click();
    a.remove();
};