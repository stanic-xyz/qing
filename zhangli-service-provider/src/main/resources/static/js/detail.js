

function onbtnrecommend(argument) {
	var comments_block = document.getElementById('comments_block');
	var recommend_block = document.getElementById('recommend_block');
	var btncomment = document.getElementById('btncomment');
	var btnrecommend = document.getElementById('btnrecommend');
	
	comments_block.hidden = 'hidden';
	recommend_block.hidden = null;
	btncomment.classList.remove('switchbtn_current');
	btnrecommend.classList.add('switchbtn_current');
}

function onbtncomment(argument) {
	var comments_block = document.getElementById('comments_block');
	var recommend_block = document.getElementById('recommend_block');
	var btncomment = document.getElementById('btncomment');
	var btnrecommend = document.getElementById('btnrecommend');

	comments_block.hidden = null;
	recommend_block.hidden = 'hidden';
	btnrecommend.classList.remove('switchbtn_current');
	btncomment.classList.add('switchbtn_current');
}

