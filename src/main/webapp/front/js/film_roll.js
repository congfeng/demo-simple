// JavaScript Document

$(function() {
fr = new FilmRoll({
container: '#film_roll_slider',
animation: 1000,
interval: 4000,
no_css: true,
pager: false,
prev: '#film_roll_prev',
next: '#film_roll_next'
});
});

$(function() {
$("#film_roll_slider,#film_roll_arrow").hover( 
function(){
$("#film_roll_arrow").stop(true).fadeTo('normal', 1.0);
},
function(){
$("#film_roll_arrow").fadeTo('fast', 0);
});

$(".film_roll_pager a:eq(0)").attr("href", "#")
$(".film_roll_pager a:eq(1)").attr("href", "#")
$(".film_roll_pager a:eq(2)").attr("href", "#")
$(".film_roll_pager a:eq(3)").attr("href", "#")
$(".film_roll_pager a:eq(4)").attr("href", "#")
});