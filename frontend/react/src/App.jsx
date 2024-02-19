import { useEffect } from 'react'

import SidebarWithHeader from './shared/Sidebar'
import { getCustomers } from './services/client'

function App() {

  useEffect(() => {
    getCustomers().then((res) => {
      console.log(res)
    })
    .catch((e) => {
      console.log(e)
    })
  }, [])

  return (
    <>
      <SidebarWithHeader>
      </SidebarWithHeader>
    </>
  )
}

export default App
