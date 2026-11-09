import { validate as isUuid } from 'uuid'

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
  return post('/users', { joinCode, name, email })
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
  if (!isUuid(houseId)) {
    throw new Error(`${houseId} is not a valid house ID`)
  }
  return get(`/houses/${encodeURIComponent(houseId)}/housemates`)
}

export function getChores(houseId) {
  if (!isUuid(houseId)) {
    throw new Error(`${houseId} is not a valid house ID`)
  }
  return get(`/houses/${encodeURIComponent(houseId)}/chores`)
}

export function createChore(houseId, name, intervalDays, effort) {
  if (!isUuid(houseId)) {
    throw new Error(`${houseId} is not a valid house ID`)
  }
  return post(`/houses/${encodeURIComponent(houseId)}/chores`, { name, intervalDays, effort })
}
