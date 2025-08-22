$(document).ready(function(){

  // エラーダイアログViewModel
  function ErrorModalDlgVM(error){
    var self = this;
    self.error = ko.observable(error);
  };

  // ルートViewModel
  function RootVM(){
    var self = this;

    self.UKEZUMI_ROW_COUNT = 3;
    self.UKEZUMI_COL_COUNT = 7;
    self.UKEZUMI_FUZAI_COL_COUNT = 1;
    

    self.reloadSec = ko.observable();
    self.kenchuMsg = ko.observable();
    self.ukezumiMsg = ko.observable();
    self.kensasituArray = ko.observableArray();
    self.kenchuArray = ko.observableArray();
    self.ukezumiArray = ko.observableArray();
    self.ukezumiFuzaiArray = ko.observableArray();
    self.telopMsg = ko.observableArray();
    //2025.08.15 Mod Takahashi@COSMO start テロップメッセージ：メンテナンス対応
	self.telopMsgFlg = ko.observable();
    self.errorModalDlg = ko.observable();
    self.telopMsgModalDlg = ko.observable();
    self.telopMsgUpdateModalDlg = ko.observable();
   	
    self.clickCount = 0;
    //2025.08.15 Mod Takahashi@COSMO end テロップメッセージ：メンテナンス対応

    // 設定ファイル取得
    self.setup = function(){
      async.series({
        setting: function(callback){
          var params = {
              
          };
          comm.get("setting/cr", params, callback);
        },
      }, function(err, results){
        if(err){
          throw new Error(err);
        }else{
          self.reloadSec(results.setting.reload_sec);
          self.kenchuMsg(results.setting.kenchu_msg);
          self.ukezumiMsg(results.setting.ukezumi_msg);
          self.kensasituArray(results.setting.kensasitu_array);
          self.telopMsg(results.setting.telop_msg);
          //2025.08.15 Add Takahashi@COSMO start テロップメッセージ：メンテナンス対応
          self.telopMsgFlg(results.setting.telop_flg);
          //2025.08.15 Add Takahashi@COSMO end テロップメッセージ：メンテナンス対応

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
          comm.get("update/cr", params, callback);
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

          self.ukezumiArray(results.update.ukezumi_array);
          
          self.ukezumiFuzaiArray(results.update.ukezumiFuzai_array);

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
    
        // 行と列を指定して受済番号（不在）を取得する
    self.getUkezumiFuzaiNumber = function(r, c){
      return self.ukezumiFuzaiArray()[self.UKEZUMI_FUZAI_COL_COUNT * r + c];
    };
    
    //2025.08.15 Add Takahashi@COSMO start テロップメッセージ：メンテナンス対応
    // 行と列を指定して受済番号を取得する
    self.getTelopMsg = function(){
      return self.telopMsg()[self.telopMsgFlg()];
    };
    
    // 
    window.openSetting = function(){
      self.clickCount++;
      
      if(self.clickCount >= 2){
		async.series({
	        setting: function(callback){
	          var params = {
	              
	          };
	          comm.get("setting/cr", params, callback);
	        },
     	}, function(err, results){
	        if(err){
	          throw new Error(err);
	        }else{
	          self.showTelopMsgDialog(results);
	        }
      	});
       self.clickCount=0;
      };
    }
    
    window.closeTelopMsg = function() {
    const telopMsg = document.form1.telop;

    for (let i = 0; i < telopMsg.length; i++) {
      if (telopMsg[i].checked) {//(color1[i].checked === true)と同じ
        async.series({
	        setting: function(callback){
	          var params = {
	              telopMsgFlg:telopMsg[i].value
	          };
	          comm.get("telopsetting/cr", params, callback);
	        },
     	}, function(err){
	        if(err){
	          throw new Error(err);
	        }
      	});
        break;
      }
    }
    location.reload();
  }
  
  window.closeTelopMsgUpdate = function(){
		async.series({
	        setting: function(callback){
	          var params = {
	              
	          };
	          comm.get("setting/cr", params, callback);
	        },
     	}, function(err, results){
	        if(err){
	          throw new Error(err);
	        }else{
			  
	          self.showTelopMsgDialog(results);
	        }
      	});
    }
    
    window.telopMsgUpdate = function(UpdateTelopMsg,UpdateTelopMsg1,UpdateTelopMsg2,UpdateTelopMsg3,updateFlg){
		async.series({
	        setting: function(callback){
	          if(updateFlg == 1){
		          var params = {
		              telopMsg1:UpdateTelopMsg,
		              telopMsg2:UpdateTelopMsg2,
		              telopMsg3:UpdateTelopMsg3
		          };
		      }else if(updateFlg == 2){
		          var params = {
		              telopMsg1:UpdateTelopMsg1,
		              telopMsg2:UpdateTelopMsg,
		              telopMsg3:UpdateTelopMsg3
		          };
		      }else if(updateFlg == 3){
		          var params = {
		              telopMsg1:UpdateTelopMsg1,
		              telopMsg2:UpdateTelopMsg2,
		              telopMsg3:UpdateTelopMsg
		          };
		      }
	          comm.get("telopsetting/cr", params, callback);
	        },
     	}, function(err,results){
	        if(err){
	          throw new Error(err);
	        }else{
	          window.closeTelopMsgUpdate()
	        }
      	});
      	
    }
    
  // テロップメッセージ設定画面ダイアログViewModel
  function TelopMsgModalDlgVM(results){
    var self = this;

    self.settingTelopMsg1 = results.setting.telop_msg[0];
    self.settingTelopMsg2 = results.setting.telop_msg[1];
    self.settingTelopMsg3 = results.setting.telop_msg[2];
    self.settingTelopFlg = results.setting.telop_flg;

  };
    
    // テロップメッセージ設定画面ダイアログ表示メソッド
    self.showTelopMsgDialog = function(results){
      self.telopMsgModalDlg(new TelopMsgModalDlgVM(results));
      $("#telopMsgModal").on("hidden.bs.modal", function(){
      });
      $("#telopMsgModal").modal();
    };
    
    // テロップメッセージ編集画面ダイアログViewModel
  	function TelopMsgUpdateModalDlgVM(settingTelopMsg1,settingTelopMsg2,settingTelopMsg3,updateFlg){
    var self = this;
    self.UpdateTelopMsg1 = settingTelopMsg1;
    self.UpdateTelopMsg2 = settingTelopMsg2;
    self.UpdateTelopMsg3 = settingTelopMsg3;
    self.updateFlg = updateFlg;
    
    self.updateTelopMsgCheck = function(){
		if(self.updateFlg == 1){
			return self.UpdateTelopMsg1;
		}else if(self.updateFlg == 2){
			return self.UpdateTelopMsg2;
		}else{
			return self.UpdateTelopMsg3;
		};
	};
    
    self.UpdateTelopMsg = self.updateTelopMsgCheck();
    
    };
    
    // テロップメッセージ編集画面ダイアログ表示メソッド
    window.showTelopMsgUpdateDialog = function(settingTelopMsg1,settingTelopMsg2,settingTelopMsg3,updateFlg){
      self.telopMsgUpdateModalDlg(new TelopMsgUpdateModalDlgVM(settingTelopMsg1,settingTelopMsg2,settingTelopMsg3,updateFlg));
      $("#telopMsgUpdateModal").on("hidden.bs.modal", function(){
      });
      $("#telopMsgUpdateModal").modal();
    };
    
     

    //2025.08.15 Add Takahashi@COSMO end テロップメッセージ：メンテナンス対応

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