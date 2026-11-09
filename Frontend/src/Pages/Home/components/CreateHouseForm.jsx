import { useState } from 'react'
import { createHouse } from '../api.js'

function CreateHouseForm({ onSuccess }) {
  const [error, setError] = useState('')

  // Runs when the user clicks the button
  async function handleSubmit(event) {
    event.preventDefault() // stop the browser from reloading the page

    const form = new FormData(event.target)
    const houseName = form.get('houseName')
    const name = form.get('name')
    const email = form.get('email')

    try {
      const user = await createHouse(houseName, name, email)
      onSuccess(user)
    } catch (e) {
      setError(e.message)
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>Create a house</h2>

      <p>
        <label>
          House name <input name="houseName" maxLength={50} required />
        </label>
      </p>

      <p>
        <label>
          Your name <input name="name" maxLength={50} required />
        </label>
      </p>

      <p>
        <label>
          Email <input name="email" type="email" maxLength={100} required />
        </label>
      </p>

      {error && <p>{error}</p>}

      <button type="submit">Create house</button>
    </form>
  )
}

export default CreateHouseForm
