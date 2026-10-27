import { useState, useEffect } from 'react'
import { getHousemates } from '../api.js'

function ViewHousematesList({ user }) {
  const [housemates, setHousemates] = useState([])
  const [error, setError] = useState(null)

  useEffect(() => {
    async function fetchHousemates() {
      try {
        const housematesList = await getHousemates(user.houseId)
        setHousemates(housematesList)
      } catch (err) {
        setError(err.message)
      }
    }

    fetchHousemates()
  }, [user.houseId])

  return (
    <div>
      {error && <p>{error}</p>}
      <ul>
        {housemates.map((housemate) => (
          <li key={housemate.id}>{housemate.name}</li>
        ))}
      </ul>
    </div>
  )
}

export default ViewHousematesList
