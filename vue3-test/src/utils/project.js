//项目工具类



// 生成水印
export function drawWaterMark(dom, text = "我是水印", fontSize = 18, gap = 20) {
    const ctx = dom.getContext("2d");
    ctx.font = `${fontSize}px 'PingFang SC'`;
    ctx.fillStyle = "#878180";
    let horizontalWidth = ctx.measureText(text).width;
    horizontalWidth = Math.sqrt(horizontalWidth * horizontalWidth / 2) + fontSize;

    const draw = (x, y) => {
        ctx.save();
        ctx.translate(x, y);
        ctx.rotate(-45 / 180 * Math.PI);
        ctx.fillText(text, 0, 0);
        ctx.restore();
    };

    const width = dom.width;
    const height = dom.height;
    for (let i = 0; i < width; i += horizontalWidth + gap) {
        for (let j = 0; j < height; j += horizontalWidth + gap) {
            draw(i, j);
        }
    }
}