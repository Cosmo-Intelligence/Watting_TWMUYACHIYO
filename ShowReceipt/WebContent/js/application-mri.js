$(document).ready(function(){

  // エラーダイアログViewModel
  function ErrorModalDlgVM(error){
    var self = this;
    self.error = ko.observable(error);
  };

  // ルートViewModel
  function RootVM(){
    var self = this;

	self.KENCHU_ROW_COUNT = 1;
	self.KENCHU_COL_COUNT = 2;
	self.JUNBICHU_COL_COUNT = 4;
    self.UKEZUMI_ROW_COUNT = 1;
    self.UKEZUMI_COL_COUNT = 8;

    self.reloadSec = ko.observable();
    self.kenchuMsg = ko.observable();
    self.junbichuMsg = ko.observable();
    self.ukezumiMsg = ko.observable();
    self.kensasituArray = ko.observableArray();
    self.kenchuArray = ko.observableArray();
    self.junbichuArray = ko.observableArray();
    self.ukezumiArray = ko.observableArray();
    self.telopMsg = ko.observableArray();

    self.errorModalDlg = ko.observable();

    // 設定ファイル取得
    self.setup = function(){
      async.series({
        setting: function(callback){
          var params = {
          };
          comm.get("setting/mri", params, callback);
        },
      }, function(err, results){
        if(err){
          throw new Error(err);
        }else{
          self.reloadSec(results.setting.reload_sec);
          self.kenchuMsg(results.setting.kenchu_msg);
          self.junbichuMsg(results.setting.junbichu_msg);
          self.ukezumiMsg(results.setting.ukezumi_msg);
          self.kensasituArray(results.setting.kensasitu_array);
          self.telopMsg(results.setting.telop_msg);

          self.update();
        }
      });
    };

    // 受付番号更新
    var updateTimer = null;
    self.update = function(){
      async.series({
        update: function(callback){
          var params = {
          };
          comm.get("update/mri", params, callback);
        },
      }, function(err, results){
        if(err){
          throw new Error(err);
        }else{
          var oldKenchu = self.kenchuArray();
          self.kenchuArray(results.update.kenchu_array);
          ko.utils.arrayForEach(self.kenchuArray(), function(item, index){
            if(item && (item != oldKenchu[index])){
              self.blinkKenchu(index);
            }
          });
          
          self.junbichuArray(results.update.junbichu_array);

          self.ukezumiArray(results.update.ukezumi_array);
          
          clearTimeout(updateTimer);
          updateTimer = setTimeout(function(){
            self.update();
          }, self.reloadSec() * 1000);
        }
      });
    };

    // 検中欄のID取得
    self.getKenchuElementId = function(index){
      return "kenchu_" + index;
    }

    // 検中欄を点滅させる
    self.blinkKenchu = function(index){
      var target = $("#" + self.getKenchuElementId(index));
      target.removeClass("blink").addClass("blink").one('webkitAnimationEnd MSAnimationEnd animationend', function(){target.removeClass("blink")});
    }

    // 行と列を指定して受済番号を取得する
    self.getUkezumiNumber = function(r, c){
      return self.ukezumiArray()[self.UKEZUMI_COL_COUNT * r + c];
    };

    // エラーダイアログ表示メソッド
    self.showErrorDialog = function(error, callback){
      self.errorModalDlg(new ErrorModalDlgVM(error));
      $("#errorModal").on("hidden.bs.modal", function(){
        if(callback){
          callback();
        }
      });
      $("#errorModal").modal();
    };

    // エラーハンドラ
    window.onerror = function (msg, file, line, column, err) {
      var s =
        "msg: " + msg + "\n" +
        "file: " + file + "\n" +
        "line: " + line + "\n";
      if(column){
        s += "column: " + column + "\n";
      }
      if(err){
        s += "stack: " + err.stack+ "\n";
      }
      self.showErrorDialog(
        s,
        function(){
          closeWindow();
        }
      );
    };

  };

  rootVM = new RootVM();
  ko.applyBindings(rootVM);
  rootVM.setup();

  // 画面サイズ調整
  function resize(){
    // 拡大率(縮小率) = 実際のウィンドウ横幅 / BODY要素の指定横幅
    var zoom = Math.floor(window.innerWidth / $("body").width() * 100);
    $("body").css("zoom", zoom + "%");
    var bodyMargin = Math.floor((window.innerWidth - ($("body").width() * zoom / 100)) / 2);
    $("body").css("margin-left", bodyMargin + "px");
  };

  var timer = null;
  $(window).on("resize", function(){
    clearTimeout(timer);
    timer = setTimeout(function(){
      resize();
    }, 300);
  });

  resize();

  // ESCキーで終了
  window.onkeydown = function(e){
    if(e.keyCode == 27){
      closeWindow();
    }
  };

  // アプリケーション終了
  function closeWindow(){
    window.open('about:blank','_self').close();
  }
  
  // テロップを流す
  function animateText() {
    var textWidth = $(".telop-text").width();
    var containerWidth = $(".telop-container").width();

    $(".telop-text").css("left", containerWidth);

    $(".telop-text").animate({
        left: -textWidth
    }, 20000, "linear", function() {
        animateText();
    });
  }

  animateText();

});