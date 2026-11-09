import CreateHouseForm from './components/CreateHouseForm.jsx'
import JoinHouseForm from './components/JoinHouseForm.jsx'

function HomePage({ onSuccess }) {
  return (
    <main>
      <h1>Housemate Solution</h1>
      <CreateHouseForm onSuccess={onSuccess} />
      <JoinHouseForm onSuccess={onSuccess} />
    </main>
  )
}

export default HomePage
