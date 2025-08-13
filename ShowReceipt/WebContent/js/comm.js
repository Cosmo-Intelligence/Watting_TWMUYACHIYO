// 通信タイムアウト、キャッシュ設定
$.ajaxSetup(
  {
    timeout: 180000,
    cache: false
  }
);

var comm = {
  get: function(url, params, asyncCallback){
    $.getJSON("rest/" + url, params, function(data, status, xhr){
      if(data.result != "OK"){
        asyncCallback(comm.createServerError(data));
      }else{
        asyncCallback(null, data);
      }
    }).fail(function(jqxhr, status, error){
      asyncCallback(comm.createCommError(url, jqxhr, error));
    });
  },

  post: function(url, params, asyncCallback){
    $.post("rest/" + url, params, function(data, status, xhr){
      if(data.result != "OK"){
        asyncCallback(comm.createServerError(data));
      }else{
        asyncCallback(null, data);
      }
    }, "json").fail(function(jqxhr, status, error){
      asyncCallback(comm.createCommError(url, jqxhr, error));
    });
  },

  createCommError: function(url, jqxhr, error){
    if(error == "timeout"){
      var e = new Error("通信がタイムアウトしました。");
      e.errlevel = "WARN";
      return e;
    }

    if(jqxhr.status == 200){
      return error;
    }

    var e = new Error("url: " + url + "; detail: " + JSON.stringify(jqxhr));
    e.name = "CommError";
    return e;
  },

  createServerError: function(data){
    var e = new Error();
    if(data.msg){
      e.message = data.msg;
    }
    if(data.errlevel){
      e.errlevel = data.errlevel;
    }
    return e;
  },

};