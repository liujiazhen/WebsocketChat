<%--
  Created by IntelliJ IDEA.
  User: liujiazhen
  Date: 2019/4/30
  Time: 13:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>狗子聊天室</title>
</head>
<body>
Welcome<br/><input id="text" type="text"/>
<button onclick="send()">发送消息</button>
<hr/>
<button onclick="closeWebSocket()">退出</button>
<hr/>
<div id="message"></div>
</body>

<script type="text/javascript">
    var name = prompt("给自己起个狗名！默认名字为二狗子","");


    var websocket = null;
    //判断当前浏览器是否支持WebSocket
    if (name == null || name.length < 1) {
        name = "二狗子" + Math.round(Math.random() * 1000); ;
    }
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://localhost:8080/websocket_Web_exploded/websocket/" + name);
    } else {
        alert('你的浏览器 版本太Low，赶紧卸载了吧！')
    }

    //连接发生错误的回调方法
    websocket.onerror = function () {
        setMessageInnerHTML("加入狗子聊天室发生错误");
    };

    //连接成功建立的回调方法
    websocket.onopen = function () {
        setMessageInnerHTML("加入狗子聊天室成功！");
    }

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        setMessageInnerHTML(event.data);
    }

    //连接关闭的回调方法
    websocket.onclose = function () {
        setMessageInnerHTML("退出狗子聊天室！");
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        closeWebSocket();
    }

    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML) {
        document.getElementById('message').innerHTML += innerHTML + '<br/>';
    }

    //关闭WebSocket连接
    function closeWebSocket() {
        websocket.close();
    }

    //发送消息
    function send() {
        var message = document.getElementById('text').value;
        websocket.send(message);
    }
</script>
</html>
