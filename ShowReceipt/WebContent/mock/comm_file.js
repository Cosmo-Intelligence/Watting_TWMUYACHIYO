var comm = {
  wait: 100,

  get: function(url, params, asyncCallback){
    setTimeout(function(){
      var param = $.param(util.removeUndefinedValue(params));
      if(param){
        param = "!" + param;
      }
      $.getJSON("mock/json_test_data/" + url + param + ".txt", null, function(data, status, xhr){
        if(data.result != "OK"){
          asyncCallback(comm.createServerError(data));
        }else{
          asyncCallback(null, data);
        }
      }).fail(function(jqxhr, status, error){
        if(params && ((jqxhr.status == 404) || ((jqxhr.status == 0) && (status == "error")))){
          comm.get(url, null, asyncCallback);
        }else{
          asyncCallback(comm.createCommError(url, jqxhr, error));
        }
      });
    }, comm.wait);
  },

  post: function(url, params, asyncCallback){
    comm.get(url, params, asyncCallback);
  },

  createCommError: function(url, jqxhr, error){
    if(jqxhr.status == 200){
      return error;
    }else{
      var e = new Error("url: " + url + "; detail: " + JSON.stringify(jqxhr));
      e.name = "CommError";

      return e;
    }
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
