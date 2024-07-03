//静态资源工具类

export const getImageUrl=(fileName)=>{
    return import.meta.env.VITE_BASE_API+'/resource/images/' + fileName;
}