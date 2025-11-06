// JavaScript Document
$(function(){
	$(window).load(function(e) {
		$(".ly_btn").click(function() {
			var maskH = $(document).height(); //html전체높이를 maskH란 변수로 선언(꽉찬 마스크)
			var maskW = $(document).width();  //html전체넓이를 maskW란 변수로 선언(꽉찬 마스크)
			var lyH = $(window).height(); //보이는 화면 높이를 lyH란 변수로 선언(중앙정렬)
			var lyW = $(window).width(); //보이는 화면 넓이를 lyW란 변수로 선언(중앙정렬)	
			var ly = $(this).attr("alt"); // 클릭한 .ly_btn 의 alt속성을 ly란 변수로 선언 (alt값 추출의도)
			//$('html').css("overflow","hidden"); // 레이어 뜬상태에서 html 스크롤바 삭제...
			$('#ly_mask').css({'width':maskW,'height':maskH});  //마스크 전체영역 덮기
			$('#ly_mask').fadeTo('300',0.5); // 클릭한 .ly_btn 의 alt속성을 ly란 변수로 선언 (alt값 추출의도)
			$('.'+ly).css('top', $(window).scrollTop() + lyH/2 - $('.'+ly).height()/2); //스크롤된만큼의 높이를 더하고 레이어 세로 중앙
			$('.'+ly).css('left',lyW/2-$('.'+ly).width()/2).show();	//레이어 가로 중앙	
			$('#ly_mask').click(function () {
				$('html').css("overflow","auto");//스크롤바 활성화 
				$('#ly_mask,.'+ly).hide();	// 마스크 없애기
			});	
		});
	});	
});
