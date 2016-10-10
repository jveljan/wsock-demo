$(function() {

$nick = $('#nickname');
$input = $('#msg');
$messages = $('#messages');
$sendMsgForm = $('#sendMsgForm');
$changeNickForm = $('#changeNickForm');
$userCount = $('#userCount');

  var token = Math.random(); // does not matter, server accepts us anyway
  var ws = new Wsock('/chat', token);
  ws.connect(function() {
    console.log('we are connected');
    //get our assigned name
    ws.send('/get/nick', {}, function(nick) {
      $nick.val(nick);
    });
    ws.send('/get/user/count', {}, function(resp) {
      $userCount.text(resp);
    });
    ws.on('/user/count/change', function(resp) {
      $userCount.text(resp);
    });
    $changeNickForm.on('submit', function(e) {
      e.preventDefault();
      ws.send('/set/nick', $nick.val());
      $input.focus();
    });
    //send message action
    $sendMsgForm.on('submit', function(e) {
      e.preventDefault();
      var msg = $input.val();
      //clear
      $input.val('');
      ws.send('/send/message', msg);
    });
    // receive message event
    ws.on('/message', function(mb) {
      console.log('got message', mb);
      $('<div />').text(mb.nick + ": " + mb.msg)
        .appendTo($messages);
    });
    ws.on('/total', function() {
      console.log('on /total', arguments);
    });
  });
});