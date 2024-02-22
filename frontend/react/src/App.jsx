import { useEffect, useState } from "react";

import SidebarWithHeader from "./components/shared/Sidebar";
import { getCustomers } from "./services/client";
import { Spinner } from "@chakra-ui/react";
import { Wrap, WrapItem } from '@chakra-ui/react'
import SocialProfileWithImage from "./components/shared/CustomerCard";

function App() {
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);

    getCustomers()
      .then((res) => {
        console.log(res);
        setCustomers(res.data);
      })
      .catch((e) => {
        console.log(e);
      })
      .then(() => {
        setLoading(false);
      });
  }, []);

  return (
    <>
      <SidebarWithHeader>
        {customers.length === 0 && loading ? (
          <Spinner
            thickness="4px"
            speed="0.65s"
            emptyColor="gray.200"
            color="blue.500"
            size="xl"
          />
        ) : (
          <>
            <Wrap spacing='30px' justify='center'>
            {
            customers.length > 0 ?
            customers.map((customer) => (
              <WrapItem key={customer.id}>
                <SocialProfileWithImage
                  name={customer.name}
                  email={customer.email}
                  age={customer.age}
                  gender={customer.gender}
                />
              </WrapItem>
            )) : <p>No customers found</p>
            }
            </Wrap>
          </>
        )}
      </SidebarWithHeader>
    </>
  );
}

export default App;
