const imageDrop = document.getElementById("imageDrop");
const imageInput = document.getElementById("imageInput");
const imagePreview = document.getElementById("imagePreview");

const resumeDrop = document.getElementById("resumeDrop");
const resumeInput = document.getElementById("resumeInput");
const resumeName = document.getElementById("resumeName");

// Setup drag & drop
function setupDropZone(dropZone, input, display) {

    dropZone.addEventListener("click", () => input.click());

    dropZone.addEventListener("dragover", (e) => {
        e.preventDefault();
        dropZone.style.borderColor = "#00aaff";
    });

    dropZone.addEventListener("dragleave", () => {
        dropZone.style.borderColor = "#555";
    });

    dropZone.addEventListener("drop", (e) => {
        e.preventDefault();

        const files = e.dataTransfer.files;
        if (files.length > 0) {
            input.files = files;
            handleFile(input, display);
        }

        dropZone.style.borderColor = "#555";
    });

    input.addEventListener("change", () => {
        handleFile(input, display);
    });
}

function handleFile(input, display) {
    const file = input.files[0];
    if (!file) return;

    if (file.type.startsWith("image/")) {
        const reader = new FileReader();
        reader.onload = function () {
            display.innerHTML = `<img src="${reader.result}" width="100">`;
        };
        reader.readAsDataURL(file);
    } else {
        display.innerHTML = `<p>${file.name}</p>`;
    }
}

setupDropZone(imageDrop, imageInput, imagePreview);
setupDropZone(resumeDrop, resumeInput, resumeName);


// Form Submit
document.getElementById("studentForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const formData = new FormData(this);

    fetch("http://localhost:8908/api/students/save", {
        method: "POST",
        body: formData
    })
    .then(res => res.text())
    .then(data => {
        alert(data);
        this.reset();
        imagePreview.innerHTML = "";
        resumeName.innerHTML = "";
    })
    .catch(err => console.error(err));
});