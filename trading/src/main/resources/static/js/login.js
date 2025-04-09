function login() {
    const username = document.getElementById("loginUsername").value;
    const messageBox = document.getElementById("loginMessage");

    if (!username.trim()) {
        messageBox.textContent = "Username can not be empty";
        return;
    }

    fetch(`/user/login?username=${encodeURIComponent(username)}`)
        .then(async response => {
            const text = await response.text();
            if (!response.ok) {
                throw new Error(text);
            }

            return  JSON.parse(text);
        })
        .then(data =>{
            localStorage.setItem("userId", data.id);
            localStorage.setItem("username", data.username);
            messageBox.textContent = "Successfully login!";
            window.location.href="viewCryptocurrencies.html"
        })
        .catch(error => {
            messageBox.textContent = error.message;
        });
}