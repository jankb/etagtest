
document.getElementById('getDataButton').addEventListener('click', fetchData);
//document.getElementById('addLineButton').addEventListener('click', addEmptyLine);
document.getElementById('saveButton').addEventListener('click', updateAllCountries);

function fetchData(myform) {
  fetch("http://localhost:8181/country", {
      })
  .then(response =>  {
      if (!response.ok) {
      throw new Error('Network response was not ok: ' + response.statusText)
      }

       const etag = response.headers.get('ETag');
       const table = document.getElementById('data-table');
       table.setAttribute('data-etag', etag);

      return response.json();
      })
  .then(data => {
      populateTable(data.countries);
      console.log(data.countries);
      })
  .catch(error => console.error('Error fetching data: ', error));
}

function populateTable(countries){
  const tableBody = document.getElementById('data-table').getElementsByTagName('tbody')[0];

  tableBody.innerHTML = "";

  countries.forEach(country => {
      console.log(country);
      const newRow = tableBody.insertRow();

      const idCell = newRow.insertCell(0);
      const nameCell = newRow.insertCell(1);
      const populationCell = newRow.insertCell(2);

      idCell.innerHTML = `<input type="text" readonly value="${country.id}">`;
      nameCell.innerHTML = `<input type="text" value="${country.name}">`;
      populationCell.innerHTML = `<input type="text" value="${country.population}">`;
      }
      );
}

function addEmptyLine() {
  const tableBody = document.getElementById('data-table').getElementsByTagName('tbody')[0];
  const newRow = tableBody.insertRow();

  const idCell = newRow.insertCell(0);
  const nameCell = newRow.insertCell(1);
  const populationCell = newRow.insertCell(2);

  idCell.innerHTML = `<input type="text" readonly value="">`;
  nameCell.innerHTML = `<input type="text" value="">`;
  populationCell.innerHTML = `<input type="text" value="">`;

}

function updateAllCountries() {
  const tableBody = document.getElementById('data-table').getElementsByTagName('tbody')[0];
  const rows = Array.from(tableBody.getElementsByTagName('tr'));

  const allCountries = rows.map(row => {
    const id = parseInt(row.cells[0].querySelector('input').value, 10);
    const name = row.cells[1].querySelector('input').value;
    const population = parseInt(row.cells[2].querySelector('input').value, 10);
    return {
      id,
      name,
      population
    };
  });

  // Assuming you use the same ETag for all countries, we'll pick the first one as an example.
 // const etag = etags[allCountries[0]?.id]; // Make sure this line makes sense in your actual use case.
  const table = document.getElementById('data-table');
  const etag = table.getAttribute('data-etag');


 // fetch('http://localhost:8181/country/updateAll', {
  fetch('http://localhost:8181/country', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'If-None-Match': etag
    },
    body: JSON.stringify({ countries: allCountries })
  })
  .then(response => {
    if (response.status === 412) {
      alert('Data has been modified by another client. Please refresh and try again.');
      return;
    }

    if (!response.ok) {
      throw new Error('Failed to update data.');
    }

    return response.json();
  })
  .then(updatedData => {
    alert('All data updated successfully.');
  })
  .catch(error => {
    console.error('Error updating data:', error);
  });
}
