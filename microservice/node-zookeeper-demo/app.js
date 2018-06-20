//1  简单处理
/*
var http=require('http');
var fs=require('fs');

var PORT=8084;

var app=http.createServer(function (req,res) {
    var path=__dirname+req.url;
    console.log(__dirname);
    fs.readFile(path,function (err,data) {
        if(err){
            res.end();
            return;
        }
        res.write(data.toString());
        res.end();
    })
});

app.listen(PORT,function () {
    console.log("server is running at %d",PORT);
})
*/
//2  express处理
/*
var  express=require('express');

var PORT=8084;

var app=express();

app.use(express.static("."));

app.listen(PORT, function () {
   console.log('server is running at %d',PORT);
});

app.get('/hello',function (req,res){
   res.send("hello");
})*/

//3 集群处理
var express=require('express');
var cluster=require('cluster');
var os=require('os');

var PORT=8080;
var CPUS=os.cpus().length;//获取CPU内核数

console.log(CPUS);
if(cluster.isMaster){
    //当前进程为主进程
    for(var i=0;i<CPUS;i++){
        cluster.fork();
    }
}else{
    //当前进程为子进程
    var app=express();

    app.use(express.static("."));

    app.listen(PORT, function () {
        console.log('server is running at %d',PORT);
    });

    app.get("/hello",function (req,res) {
        res.send("hello");
    })
}
