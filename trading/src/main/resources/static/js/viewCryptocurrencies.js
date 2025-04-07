let username = localStorage.getItem("username")
const welcome = document.getElementById("title");
welcome.textContent = "Welcome " + username;
welcome.style.display = "block";

function logout() {
    localStorage.removeItem("userId");
    localStorage.removeItem("username");

    alert("Successfully exit");
    window.location.href = "login.html";
}


const ws = new WebSocket("wss://ws.kraken.com/v2");

const trackedSymbols = [
    "BTC/USD",
    "ETH/USD",
    "ADA/USD",  // Cardano
    "SOL/USD"   // Solana
];

ws.onopen = () => {
    console.log("WebSocket връзката е отворена");

    ws.send(JSON.stringify({
        method: "subscribe",
        params: {
            channel: "ticker",
            symbol: trackedSymbols
        },
    }));
};

// глобални променливи
let firstUpdateReceived = false;
let currentPrices = {};

ws.onmessage = (event) => {
    const msg = JSON.parse(event.data);

    if (msg.channel === "ticker" && msg.type === "snapshot" && msg.data) {
        msg.data.forEach((crypto) => {
            console.log("Crypto object:", crypto);

            const symbol = crypto.symbol;
            const priceForBuy = crypto.ask;
            const priceForSell = crypto.bid;
            updateOrInsert(symbol, priceForBuy, priceForSell);
            currentPrices[symbol] = {
                bid: crypto.bid,
                ask: crypto.ask
            };
            const td = document.querySelector(`.sell-price[data-symbol="${symbol}"]`);
            if (td) {
                td.textContent = `$${parseFloat(priceForSell).toFixed(2)}`;
            }
            if (!firstUpdateReceived) {
                firstUpdateReceived = true;
                loadUserAssets(); //извикваме я тука, защото вече тук имам валидни данни
            }
        });
    }


    if (msg.channel === "ticker" && msg.type === "update" && msg.data) {
        msg.data.forEach((crypto) => {
            const symbol = crypto.symbol;
            const priceForBuy = crypto.ask;
            const priceForSell = crypto.bid;
            currentPrices[symbol] = {
                bid: crypto.bid,
                ask: crypto.ask
            };
            const td = document.querySelector(`.sell-price[data-symbol="${symbol}"]`);
            if (td) {
                td.textContent = `$${parseFloat(priceForSell).toFixed(2)}`;
            }

            if (priceForBuy || priceForSell) {
                updateOrInsert(symbol, priceForBuy, priceForSell);
            }
        });
    }

};

function updateOrInsert(symbol, priceForBuy, priceForSell) {
    const tbody = document.getElementById("cryptoBody");
    let row = document.getElementById(symbol);

    if (!row) {
        row = document.createElement("tr");
        row.id = symbol;
        row.innerHTML = `
      <td>${symbol}</td>
      <td class="priceForBuy">$${parseFloat(priceForBuy).toFixed(2)}</td>
      <td class="priceForSell">$${parseFloat(priceForSell).toFixed(2)}</td>
      <td><input type="number" class="input-quantity" step="0.01" min="0.01"></td>
      <td><button type="button" onclick="buyCrypto(this,'${symbol}', ${priceForBuy})">Buy</button></td>
    `;
        tbody.appendChild(row);
    } else {
        row.querySelector(".priceForBuy").textContent = `$${parseFloat(priceForBuy).toFixed(2)}`;
        row.querySelector(".priceForSell").textContent = `$${parseFloat(priceForSell).toFixed(2)}`;
    }
}

function buyCrypto(button, symbol, priceForBuy) {
    const row = button.closest("tr"); // намира реда на бутона
    const input = row.querySelector(".input-quantity"); // търси input само в този ред
    const quantity = parseFloat(input.value);

    let obj = {
        "userId": parseInt(localStorage.getItem("userId")),
        "symbol": symbol,
        "quantity": quantity,
        "type": "BUY",
        "price": priceForBuy,
        "profitLoss": null,

    }

    fetch("/transaction/buy", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(obj)
    })
        .then(async response => {
            console.log("response:", response)
            if (!response.ok) {
                throw new Error(await response.text());
            }
            return response.text();
        })
        .then(data => {
            loadUserAssets();
            document.querySelectorAll(".input-quantity").forEach(input => input.value = "");
            alert(data);
        })
        .catch(err => {
            alert(err.message);
        })
}


let userAssets = [];


function loadUserAssets() {
    let userId = parseInt(localStorage.getItem("userId"));
    fetch(`/user-assets/${userId}`)
        .then(res => res.json())
        .then(data => {
            userAssets = data;
            updateUserAssets();
        })
        .catch(err => alert(err.message));
}

function updateUserAssets() {
    const tbody = document.getElementById("userAssetsBody");
    tbody.innerHTML = "";

    userAssets.forEach(asset => {
        const symbol = asset.symbol;
        const price = currentPrices[symbol]?.bid;

        const row = document.createElement("tr");
        row.innerHTML = `
                <td>${symbol}</td>
                <td class="sell-price" data-symbol="${symbol}"> ${price ? `$${parseFloat(price).toFixed(2)}` : 'Loading...'} </td>
                <td>${parseFloat(asset.quantity).toFixed(4)}</td>
                <td><input type="number" class="input-quantity-for-sale" min="0.01" max="${asset.quantity}" step="0.01"></td>
                <td> <button onclick="sellCrypto(this, '${symbol}', ${price})">Sell</button> </td>
            `;
        tbody.appendChild(row);
    });
}

function sellCrypto(button, symbol, price) {
    const row = button.closest("tr");
    const input = row.querySelector(".input-quantity-for-sale");
    const quantity = parseFloat(input.value);

    if (!quantity || quantity <= 0) {
        alert("Invalid quantity. It will have to be more than 0");
        return;
    }

    let sellObj = {
        userId: parseInt(localStorage.getItem("userId")),
        symbol: symbol,
        quantity: quantity,
        type: "SELL",
        price: price,
        profitLoss: null
    };

    fetch("/transaction/sell", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(sellObj)
    })
        .then(async response => {
            if (!response.ok) {
                throw new Error(await response.text());
            }
            return response.text();
        })
        .then(data => {
            alert(data);
            loadUserAssets();
        })
        .catch(error => {
            alert(error.message);
        })
}

document.getElementById("table-for-sale").addEventListener("click", function () {
    let table = document.getElementById("sellTable");

    if (table.style.display === "none") {
        table.style.display = "block";
        document.getElementById("userInformationDiv").style.display = "none";
        document.getElementById("transactionTable").style.display = "none";
    } else {
        table.style.display = "none"
    }
});

function viewUserInformation() {
    let userInformationDiv = document.getElementById("userInformationDiv");

    if (userInformationDiv.style.display === "none") {
        userInformationDiv.style.display = "block"
        document.getElementById("cryptoTable").style.display = "none";
        document.getElementById("sellTable").style.display = "none";
        document.getElementById("transactionTable").style.display = "none";
    } else {
        userInformationDiv.style.display = "none"
    }

    userInformationDiv.innerHTML = `
            <button type="button" onclick="viewTransactions()">History of transactions</button>
            <button type="button" onclick="resetAccount()">Reset Account</button>
       `;
}

function resetAccount() {
    let userId = localStorage.getItem("userId");
    document.getElementById("transactionTable").style.display = "none";

    fetch(`/user/reset/by/id/${userId}`)
        .then(async response => {
            if (!response.ok) {
                throw new Error(await response.text());
            }
            return response.text()
        })
        .then(data => {
            alert(data);
            loadUserAssets();
        })
        .catch(error => {
            alert(error);
        })
}

document.getElementById("table-for-buy").addEventListener("click", function () {
    let table = document.getElementById("cryptoTable");

    if (table.style.display === "none") {
        table.style.display = "block";
        document.getElementById("userInformationDiv").style.display = "none";
        document.getElementById("transactionTable").style.display = "none";
    } else {
        table.style.display = "none"
    }
});

function viewTransactions() {
    let userId = localStorage.getItem("userId");
    let transactionTable = document.getElementById("transactionTable");
    if (transactionTable.style.display === "none") {
        transactionTable.style.display = "block";
        document.getElementById("cryptoTable").style.display = "none";
        document.getElementById("sellTable").style.display = "none";
    } else {
        transactionTable.style.display = "none";
    }

    fetch(`/transaction/history/by/user-id/${userId}`)
        .then(response => {
            /*if (!response.ok) {
                throw new Error("Invalid response");
            }*/
            return response.json();
        })
        .then(data => {
            const tbody = document.getElementById("transactionBody");
            tbody.innerHTML = "";

            data.forEach(transaction => {
                const row = document.createElement("tr");
                row.innerHTML = `
                      <td>${transaction.symbol}</td>
                      <td>${transaction.type}</td>
                      <td>${parseFloat(transaction.quantity).toFixed(4)}</td>
                      <td>$${parseFloat(transaction.price).toFixed(2)}</td>
                      <td>${transaction.profitLoss !== null ? '$' + parseFloat(transaction.profitLoss).toFixed(2) : '-'}</td>
                      <td>${new Date(transaction.localDateTime).toLocaleString()}</td>
                    `;

                tbody.appendChild(row);
            })
        })
        .catch(error => {
            alert(error);
        })
}