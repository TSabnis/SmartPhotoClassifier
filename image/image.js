$(document).ready(function(){
	initialize();
});

function initialize_image(image) {
	$("#center_img").attr("src", image);
}

function initialize_face_tags(faces) {
	faces.forEach(function(elem) {
		plotRectangle(elem);
	});
}

function initialize() {
	//pass face location from jsp instead of dummy values below
	var faces = [{ "name": "Person Name", "x":20, "y":20, "w":50, "h":50}, { "name": "Person2 Name", "x":100, "y":100, "w":100, "h":100}]
    // pass image urls from jsp instead of dummy values below
    var imagesUrls = ['arizona.jpg','book.jpg','logo.png'];
    initialize_image(imagesUrls[0]);
    initialize_face_tags(faces);
}

var plotRectangle = function(face) {
        var rect = document.createElement('div');
        var arrow = document.createElement('div');
        var input = document.createElement('input');
        rect.onclick = function name() {
          input.select();
        };
        input.value = face.name;
        arrow.classList.add('arrow');
        rect.classList.add('rect');
        rect.appendChild(input);
        rect.appendChild(arrow);
        $('#photo').append(rect);
        rect.style.width = face.w + 'px';
        rect.style.height = face.h + 'px';
        rect.style.left = (img.offsetLeft + face.x) + 'px';
        rect.style.top = (img.offsetTop + face.y) + 'px';
      };