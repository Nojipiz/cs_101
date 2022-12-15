import { useContext } from 'react';
import Modal from 'react-modal';
import { HomeContext } from '../HomePage/HomePage';
import styles from './styles.module.css';
import ChartResult from './ChartResult';

Modal.setAppElement('#root');

export default function ResultModal() {
  const { result, setResult, image } = useContext(HomeContext);
  const customStyles = {
    content: {
      top: '50%',
      left: '50%',
      right: 'auto',
      bottom: 'auto',
      marginRight: '-50%',
      transform: 'translate(-50%, -50%)'
    },
    overlay: {
      background: 'rgba(0, 0, 0, 0.5)'
    }
  };

  return (
    <Modal
      isOpen={result != undefined}
      contentLabel="Result Modal"
      style={customStyles}
    >
      <div className={styles.container}>
        <h1 className={styles.title}>Resultado</h1>
        {result && (
          <>
            <h2 className={styles.result}>
              {result > 0.5 ? 'Araña' : 'Mosca'}
            </h2>
            {/* <p className={styles.fiability}>
              {'Fiabilidad: ' +
                (result > 0.5 ? result * 100 : (1 - result) * 100)}
            </p>
            <p className={styles.result_data}>{'Resultado: ' + result}</p> */}
            <ChartResult 
            data1 = {(result > 0.5 ? result * 100 : (1 - result) * 100)}
            data2 = {(1 - result)*100}
            />
          </>

        )}
        {image && (
          <img
            className={styles.image}
            src={'data:image/png;base64,' + image}
          />
        )}
        <button
          className={styles.close_button}
          onClick={() => setResult(undefined)}
        >
          Aceptar
        </button>
      </div>
    </Modal>
  );
}
