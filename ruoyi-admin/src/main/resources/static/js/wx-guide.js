// 微信内打开引导：判断微信 / 显示引导 / 金额展示 / 复制链接
function isWeChat() {
    return /MicroMessenger/i.test(navigator.userAgent);
}
function showWxGuide() {
    var guide = document.getElementById('wxGuide');
    if (guide) {
        guide.style.display = 'block';
    }
    var tip = document.getElementById('loadingTip');
    if (tip) {
        tip.style.display = 'none';
    }
    // 链接展示
    var urlBox = document.getElementById('wxGuideUrl');
    if (urlBox) {
        urlBox.innerText = window.location.href;
    }
    // 金额展示：从页面金额元素读取
    var amountEl = document.getElementById('amount');
    var amountBox = document.getElementById('wxGuideAmount');
    if (amountEl && amountBox && amountEl.value) {
        var num = parseFloat(amountEl.value);
        amountBox.innerText = '¥ ' + (isNaN(num) ? amountEl.value : num.toFixed(2));
    }
}
function copyUrl() {
    var url = window.location.href;
    if (navigator.clipboard && navigator.clipboard.writeText) {
        navigator.clipboard.writeText(url).then(function () {
            alert('链接已复制，请在浏览器中粘贴打开');
        }, function () {
            fallbackCopy(url);
        });
    } else {
        fallbackCopy(url);
    }
}
function fallbackCopy(text) {
    var ta = document.createElement('textarea');
    ta.value = text;
    ta.style.position = 'fixed';
    ta.style.opacity = '0';
    document.body.appendChild(ta);
    ta.select();
    try {
        document.execCommand('copy');
        alert('链接已复制，请在浏览器中粘贴打开');
    } catch (e) {
        alert('复制失败，请长按上方链接手动复制');
    }
    document.body.removeChild(ta);
}
