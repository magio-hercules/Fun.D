  /*--------------------------
              Managerment introduce 
              ---------------------------- */
  $(function() {
          $("dt").click(function() {
              $(this).toggleClass("open");
              if ($(this).hasClass("open"))
                  $("dt").not(this).removeClass("open");
          })
      })
      /*---------------------
   TOP Menu Stick
  --------------------- */
  var s = $("#sticker");
  var pos = s.position();
  $(window).on("scroll", function() {
      var windowpos = $(window).scrollTop() > 300;
      if (windowpos > pos.top) {
          s.addClass("stick");
      } else {
          s.removeClass("stick");
      }
  });

  /*----------------------------
     Navbar nav
    ------------------------------ */
  var main_menu = $(".main-menu ul.navbar-nav li ");
  main_menu.on("click", function() {
      main_menu.removeClass("active");
      $(this).addClass("active");
  });
  /*----------------------------
     wow js active
    ------------------------------ */
  new WOW().init();

  $(".navbar-collapse a:not(.dropdown-toggle)").on("click", function() {
      $(".navbar-collapse.collapse").removeClass("in");
  });

  /*----------------------------
     Scrollspy js
    ------------------------------ */
  var Body = $("body");
  Body.scrollspy({
      target: ".navbar-collapse",
      offset: 80,
  });

  /*----------------------------
    Page Scroll
    ------------------------------ */
  var page_scroll = $("a.page-scroll");
  page_scroll.on("click", function(event) {
      var $anchor = $(this);
      $("html, body")
          .stop()
          .animate({
                  scrollTop: $($anchor.attr("href")).offset().top - 55,
              },
              1500,
              "easeInOutExpo"
          );
      event.preventDefault();
  });