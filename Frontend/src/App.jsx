import { useState } from 'react'
import Sidebar from './components/Sidebar.jsx'
import HomePage from './Pages/Home/HomePage.jsx'
import DashboardPage from './Pages/Dashboard/DashboardPage.jsx'
import ChoresPage from './Pages/Chores/ChoresPage.jsx'

function App() {
  const [user, setUser] = useState(null)
  const [page, setPage] = useState('dashboard')

  if (user === null) {
    return <HomePage onSuccess={setUser} />
  }

  function handleBack() {
    setUser(null)
    setPage('dashboard')
  }

  return (
    <div>
      <Sidebar currentPage={page} onNavigate={setPage} />
      {page === 'dashboard' && <DashboardPage user={user} onBack={handleBack} />}
      {page === 'chores' && <ChoresPage user={user} />}
    </div>
  )
}

export default App
