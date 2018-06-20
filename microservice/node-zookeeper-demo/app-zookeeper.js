var zookeeper=require('node-zookeeper-client');

var CONNECTION_STRING='localhost:2181';

var OPTIONS={
    sessionTimeout:5000
};

var zk=zookeeper.createClient(CONNECTION_STRING,OPTIONS);
zk.on('connected',function () {
    console.log(zk);
    zk.close();
});
zk.connect();