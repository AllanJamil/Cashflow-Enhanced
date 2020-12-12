let href = window.location.href;
let token = href.substring(href.indexOf("?") + 1, href.length);

window.location.replace('https://www.google.com?' + token);


