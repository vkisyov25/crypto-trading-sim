function saveUser() {
    let username = document.getElementById("username").value

    let obj = {
        username: username,
        balance: 10000
    }

    fetch("/user/create", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(obj)
    })
        .then(response => {
            console.log(response);
            return response.json()
        }).then(data => {
        console.log(data);
        console.log(data.errors)
        if (data.errors) {
            displayErrors(data.errors);
        } else {
            alert(data.message);
            window.location.href = "/html/login.html";
        }
    })
        .catch((error) => {
            alert("Error: " + error.message);
        });


}

function displayErrors(errors) {
    // Изчистваме предишните грешки
    document.querySelectorAll(".error-message").forEach(span => {
        span.textContent = "";
    });

    // Обхождаме грешките и ги показваме под съответните полета
    Object.keys(errors).forEach(field => {
        const errorSpan = document.getElementById(`error-${field}`);
        if (errorSpan) {
            errorSpan.textContent = Array.isArray(errors[field])
                ? errors[field].join(" ") // Обединява грешките в един текст
                : errors[field];
            errorSpan.style.color = "red";
        }
    });
}