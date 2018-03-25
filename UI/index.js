var ctx1 = document.getElementById('myChart1').getContext('2d');
var chart1 = new Chart(ctx1, {
    // The type of chart we want to create
    type: 'bar',

    // The data for our dataset
    data: {
        labels: ["03-12", "03-13", "03-14", "03-15", "03-16", "03-17", "03-18"],
        datasets: [{
            label: "Steps",
            backgroundColor: 'rgb(255, 99, 132)',
            borderColor: 'rgb(255, 99, 132)',
            data: [1000, 1030, 5030, 2309, 2098, 3033, 4512],
        }]
    },

    // Configuration options go here
    options: {
        scales: {
        yAxes: [{
            ticks: {
                beginAtZero: true
            }
        }]
    }
    }
});

var ctx2 = document.getElementById('myChart2').getContext('2d');
var chart2 = new Chart(ctx2, {
    // The type of chart we want to create
    type: 'line',

    // The data for our dataset
    data: {
        labels: ["03-12", "03-13", "03-14", "03-15", "03-16", "03-17", "03-18"],
        datasets: [{
            label: "Heart Rate and ",
            fill: false,
            // backgroundColor: 'rgb(0, 99, 132)',
            borderColor: 'rgb(0, 99, 132)',
            data: [67, 62, 55, 57, 67, 53, 70],
        }]
    },

    // Configuration options go here
    options: {
        scales: {
        yAxes: [{
            ticks: {
                max: 150,
                beginAtZero: true
            }
        }]
    }
    }
});
