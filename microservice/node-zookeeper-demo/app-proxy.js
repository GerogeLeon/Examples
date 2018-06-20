var http=require('http');
var httpProxy=require('http-proxy');

var PORT=8084;

//创建代理服务器并监听错误事件
var proxy=httpProxy.createProxyServer();
proxy.on('error',function (err,req,res) {
    res.end();//输出空白相应数据
});

var app=http.createServer(function (req,res) {
    //执行反向代理
    proxy.web(req,res,{
        target: 'http://localhost:8080/hello' //目标地址
    });
});

app.listen(PORT,function () {
    console.log('server is running at %d',PORT);
});