console.log(financialData);

let ctx = document.getElementById('netWorthChart').getContext('2d');
let chartContainer = document.getElementById('netWorthChart').parentElement;
let messageContainer = document.createElement('div');
messageContainer.className = 'center';
messageContainer.innerText = 'Add more entries to view a graph';

if (!financialData || financialData.labels.length < 2 || financialData.datasets[0].data.length < 2) {
    chartContainer.style.display = 'none';
    chartContainer.parentElement.appendChild(messageContainer);
} else {
    let myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: financialData.labels,
            datasets: [{
                label: 'Net Worth',
                data: financialData.datasets[0].data, // Access the first dataset's data array
                backgroundColor: 'rgba(75, 192, 192, 0.7)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}