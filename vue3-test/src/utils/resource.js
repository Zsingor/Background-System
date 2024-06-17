

export const getImageUrl=(fileName)=>{
    return import.meta.env.VITE_BASE_API+'/resource/' + fileName;
}