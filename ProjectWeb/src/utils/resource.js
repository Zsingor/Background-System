//静态资源工具类


/**
 * 访问文件在线地址
 * @param fileName  要访问的文件名
 */
export const getImageUrl=(fileName)=>{
    return import.meta.env.VITE_BASE_API+'/resource/images/' + fileName;
}