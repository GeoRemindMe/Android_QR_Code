$(document).ready(function() {
  $.deck('.slide');

  /*
  SyntaxHighlighter.defaults.toolbar = false;
  SyntaxHighlighter.defaults.gutter = false;
  SyntaxHighlighter.all()
  */

  $("section.titlepage").bind('deck.becameCurrent', function() {
    $("body").addClass('titlebgpage');
  });
  $("section.titlepage").bind('deck.lostCurrent', function() {
    $("body").removeClass('titlebgpage');
  });

});
