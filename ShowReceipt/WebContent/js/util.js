var util = {
  getMasterValue: function(masterArray, idCol, nameCol, id){
    var master = ko.utils.arrayFirst(masterArray, function(item){
      return item[idCol] == id;
    });
    if(master){
      return master[nameCol];
    }else{
      return null;
    }
  },

  idToName: function(masterArray, id){
    var r = util.getMasterValue(masterArray, "id", "name", id);
    if(r){
      return r;
    }else{
      return "(" + id + ")";
    }
  },

  NULL_DATE_STRING: "",

  dateToTime5: function(date){
    if(!date){
      return util.NULL_DATE_STRING;
    }

    // hh:mm形式
    var h = date.getHours();
    var m = date.getMinutes();

    return ("0" + h).slice(-2) + ":" + ("0" + m).slice(-2);
  },

  dateToDate19: function(date){
    if(!date){
      return util.NULL_DATE_STRING;
    }

    // yyyy/MM/dd hh:mm:ss形式
    return date.getFullYear().toString() + "/" +
      ("0" + (date.getMonth() + 1)).slice(-2) + "/" +
      ("0" + date.getDate()).slice(-2) + " " +
      ("0" + date.getHours()).slice(-2) + ":" +
      ("0" + date.getMinutes()).slice(-2) + ":" +
      ("0" + date.getSeconds()).slice(-2);
  },

  dateToJpDate: function(date){
    if(!date){
      return util.NULL_DATE_STRING;
    }

    // yyyy年 MM月 dd日 (day)形式
    return date.getFullYear().toString() + "年 " +
      ("0" + (date.getMonth() + 1)).slice(-2) + "月 " +
      ("0" + date.getDate()).slice(-2) + "日 (" +
      ["日","月","火","水","木","金","土"][date.getDay()] + ")";
  },

  dateToYYYYMMDD: function(date){
    if(!date){
      return util.NULL_DATE_STRING;
    }

    // yyyyMMdd形式
    return date.getFullYear().toString() +
      ("0" + (date.getMonth() + 1)).slice(-2) +
      ("0" + date.getDate()).slice(-2);
  },

  dateFromYYYYMMDD: function(yyyymmdd){
    if(!yyyymmdd){
      return null;
    }

    var d = yyyymmdd.toString();
    return new Date(d.substr(0, 4), d.substr(4, 2) - 1, d.substr(6, 2));
  },

  dateFromString: function(s){
    // yyyy/MM/dd hh:mm:ss形式 or null を想定
    if(!s){
      return null;
    }

    return new Date(s);
  },

  numToColor: function(rgb){
    return '#' + ('00000' + rgb.toString(16)).slice(-6);
  },

  zeroPadding: function(str, length){
    var s = str;
    while(s.length < length){
      s = "0" + s;
    }
    return s;
  },

  getSJISLength: function(str){
   var r = 0;
    for (var i = 0; i < str.length; i++) {
        var c = str.charCodeAt(i);
        // Shift_JIS: 0x0 ～ 0x80, 0xa0  , 0xa1   ～ 0xdf  , 0xfd   ～ 0xff
        // Unicode  : 0x0 ～ 0x80, 0xf8f0, 0xff61 ～ 0xff9f, 0xf8f1 ～ 0xf8f3
        if ( (c >= 0x0 && c < 0x81) || (c == 0xf8f0) || (c >= 0xff61 && c < 0xffa0) || (c >= 0xf8f1 && c < 0xf8f4)) {
            r += 1;
        } else {
            r += 2;
        }
    }
    return r;
  },

  removeUndefinedValue: function(obj){
    var r = {};
    for (key in obj){
      if (obj[key] !== undefined){
        r[key] = obj[key];
      }
    }
    return r;
  },

};
