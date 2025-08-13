var telop = {
  animateText: function() {
        var textWidth = $("#text").width();
        var containerWidth = $("#marquee-container").width();

        $("#text").css("left", containerWidth);

        $("#text").animate({
            left: -textWidth
        }, 5000, "linear", animateText);
    },

};