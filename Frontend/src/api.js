async function post(url, data) {
  const response = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })

  if (!response.ok) {
    const message = await response.text()
    throw new Error(message || 'Something went wrong, please try again.')
  }

  return response.json()
}

export function createHouse(houseName, name, email) {
  return post('/houses', { houseName, name, email })
}

export function joinHouse(joinCode, name, email) {
  return post('/houses/join', { joinCode, name, email })
}

// GET request helper functions for fetching data from the backend API
async function get(url) {
  const response = await fetch(url)

  if (!response.ok) {
    const message = await response.text()
    throw new Error(message || 'Something went wrong, please try again.')
  }

  return response.json()
}

export function getHousemates(houseId) {
  return get(`/houses/${houseId}/housemates`)
}
